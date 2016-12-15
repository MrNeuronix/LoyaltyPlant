package ru.poliscam.processing.service;

import com.google.common.util.concurrent.Striped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import ru.poliscam.processing.database.dao.AccountDAO;
import ru.poliscam.processing.database.dao.PaymentDAO;
import ru.poliscam.processing.database.enums.PaymentType;
import ru.poliscam.processing.database.model.Account;
import ru.poliscam.processing.database.model.Payment;
import ru.poliscam.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.processing.service.exceptions.DestinationAccountRequiredException;
import ru.poliscam.processing.service.exceptions.InsufficientMoneyException;
import ru.poliscam.processing.service.exceptions.UnknownPaymentTypeException;

@Service
public class ProcessingServiceImpl implements ProcessingService {

	private final AccountDAO accountDAO;
	private final PaymentDAO paymentDAO;
	private final LocksService locksService;

	@Autowired
	public ProcessingServiceImpl(AccountDAO accountDAO, PaymentDAO paymentDAO, LocksService locksService) {
		this.accountDAO = accountDAO;
		this.paymentDAO = paymentDAO;
		this.locksService = locksService;
	}

	@Override
	public BigDecimal getBalance(UUID number) throws AccountNotFoundException {

		// Получаем блокировку
		// Тут спорный момент - нужна ли блокировка на чтение, чтобы получить актуальную запись, если во время запроса
		// идет изменение баланса
		// В плюсах - актуальность данных, в минусах - снижение производительности от лишней блокировки
		// Будем считать, что по условиям задачи у нас критичная часть банка и актуальность данных имеет приоритет выше.
		locksService.readLock(number);

		try {
			Account account = accountDAO.findByNumber(number);

			if(account == null)
				throw new AccountNotFoundException();

			return account.getBalance();
		}
		finally	{
			locksService.readUnlock(number);
		}
	}

	@Override
	public BigDecimal addMoney(UUID number, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {
		return processMoney(number, PaymentType.PLUS, null, amount);
	}

	@Override
	public BigDecimal spentMoney(UUID number, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException{
		return processMoney(number, PaymentType.MINUS, null, amount);
	}

	@Override
	public BigDecimal transferMoney(UUID from, UUID to, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {
		return processMoney(from, PaymentType.TRANSFER, to, amount);
	}

	// Сервисный метод, реализующий операции со счетом
	@Transactional(rollbackFor = Exception.class)
	private BigDecimal processMoney(UUID number, PaymentType type, UUID to, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {

		// Получаем блокировку на запись
		locksService.writeLock(number);

		// Если передаем деньги на другой аккаунт, блокирует его тоже
		if(type.equals(PaymentType.TRANSFER)) {
			locksService.writeLock(to);
		}

		try {

			Account account = accountDAO.findByNumber(number);
			Account toAccount;

			if(type.equals(PaymentType.TRANSFER) && to == null)
				throw new DestinationAccountRequiredException();
			else
				toAccount = accountDAO.findByNumber(to);

			if(account == null || (type == PaymentType.TRANSFER && toAccount == null))
				throw new AccountNotFoundException();

			if(type.equals(PaymentType.PLUS))
				account.setBalance(account.getBalance().add(amount));
			else if(type.equals(PaymentType.MINUS)) {
				BigDecimal result = account.getBalance().add(amount.negate());

				// кредитов не выдаем
				if(result.compareTo(BigDecimal.ZERO) < 0)
					throw new InsufficientMoneyException();

				account.setBalance(result);
			}
			else if(type.equals(PaymentType.TRANSFER)) {
				BigDecimal result = account.getBalance().add(amount.negate());

				if(result.compareTo(BigDecimal.ZERO) < 0)
					throw new InsufficientMoneyException();

				account.setBalance(result);
				toAccount.setBalance(toAccount.getBalance().add(amount));

				// Сохраняем информацию о приходе денег на аккаунт
				Payment payment = new Payment(toAccount, PaymentType.TRANSFER_PLUS, account, amount);
				paymentDAO.save(payment);
			}
			else {
				// Если вдруг, каким то чудом
				throw new UnknownPaymentTypeException();
			}

			// Сохраняем информацию о транзакции
			// Если это был перевод денег, то ставим маркер, что деньги списались в результате перевода
			if(type.equals(PaymentType.TRANSFER))
				type = PaymentType.TRANSFER_MINUS;

			Payment payment = new Payment(account, type, toAccount, amount);
			paymentDAO.save(payment);

			// Сохраняем в БД
			accountDAO.save(account);

			if(toAccount != null)
				accountDAO.save(toAccount);

			return account.getBalance();
		}
		finally	{
			locksService.writeUnlock(number);

			if(to != null) {
				locksService.writeUnlock(to);
			}
		}
	}
}

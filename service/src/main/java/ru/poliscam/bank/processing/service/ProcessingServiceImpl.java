package ru.poliscam.bank.processing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import ru.poliscam.bank.processing.service.exceptions.UnknownPaymentTypeException;
import ru.poliscam.bank.processing.database.dao.AccountDAO;
import ru.poliscam.bank.processing.database.dao.PaymentDAO;
import ru.poliscam.bank.processing.database.enums.PaymentType;
import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.database.model.Payment;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.processing.service.exceptions.DestinationAccountRequiredException;
import ru.poliscam.bank.processing.service.exceptions.InsufficientMoneyException;

@Service
public class ProcessingServiceImpl implements ProcessingService {

	private final AccountDAO accountDAO;
	private final PaymentDAO paymentDAO;

	@Autowired
	public ProcessingServiceImpl(AccountDAO accountDAO, PaymentDAO paymentDAO) {
		this.accountDAO = accountDAO;
		this.paymentDAO = paymentDAO;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getBalance(UUID number) throws AccountNotFoundException {

		Account account = accountDAO.findByNumber(number);

		if(account == null)
			throw new AccountNotFoundException();

		return account.getBalance();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BigDecimal addMoney(UUID number, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {

		return processMoney(number, PaymentType.PLUS, null, amount);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BigDecimal spentMoney(UUID number, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException{

		return processMoney(number, PaymentType.MINUS, null, amount);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BigDecimal transferMoney(UUID from, UUID to, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {

			return processMoney(from, PaymentType.TRANSFER, to, amount);

	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Payment> getPayments(UUID number) throws AccountNotFoundException {

		Account account = accountDAO.findByNumber(number);

		if(account == null)
			throw new AccountNotFoundException();

		return account.getPayments();
	}

	// Сервисный метод, реализующий операции со счетом
	private BigDecimal processMoney(UUID number, PaymentType type, UUID to, BigDecimal amount)
			throws AccountNotFoundException, DestinationAccountRequiredException, UnknownPaymentTypeException,
			       InsufficientMoneyException {

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
}

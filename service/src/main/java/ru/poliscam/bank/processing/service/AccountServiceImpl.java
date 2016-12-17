package ru.poliscam.bank.processing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

import ru.poliscam.bank.processing.database.dao.AccountDAO;
import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.processing.service.exceptions.AccountNameRequiredException;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountDAO accountDAO;
	private final LocksService locksService;

	@Autowired
	public AccountServiceImpl(AccountDAO accountDAO, LocksService locksService) {
		this.accountDAO = accountDAO;
		this.locksService = locksService;
	}

	@Override
	public Account addAccount(String name) throws AccountNameRequiredException {

		if(name == null || name.isEmpty())
			throw new AccountNameRequiredException();

		Account account = new Account(name);
		return accountDAO.save(account);
	}

	@Override
	public void removeAccount(UUID number) throws AccountNotFoundException {

		// Получаем блокировку на запись
		locksService.writeLock(number);

		try {
			Account account = accountDAO.findByNumber(number);

			if (account == null)
				throw new AccountNotFoundException();

			accountDAO.delete(account);
		}
		finally {
			locksService.writeUnlock(number);
		}
	}

	@Override
	public Collection<Account> allAccounts() {
		return  (Collection<Account>) accountDAO.findAll();
	}
}

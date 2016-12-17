package ru.poliscam.bank.processing.service;

import java.util.Collection;
import java.util.UUID;

import ru.poliscam.bank.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;

public interface AccountService {
	Account addAccount(String name) throws AccountNameRequiredException;
	void removeAccount(UUID number) throws AccountNotFoundException;
	Collection<Account> allAccounts();
}

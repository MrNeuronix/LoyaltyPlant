package ru.poliscam.processing.service;

import java.util.Collection;
import java.util.UUID;

import ru.poliscam.processing.database.model.Account;
import ru.poliscam.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.processing.service.exceptions.AccountNameRequiredException;

public interface AccountService {
	Account addAccount(String name) throws AccountNameRequiredException;
	void removeAccount(UUID number) throws AccountNotFoundException;
	Collection<Account> allAccounts();
}

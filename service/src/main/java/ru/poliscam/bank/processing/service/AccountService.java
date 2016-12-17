package ru.poliscam.bank.processing.service;

import java.util.Collection;
import java.util.UUID;

import ru.poliscam.bank.processing.service.exceptions.AccountNameRequiredException;
import ru.poliscam.bank.processing.database.model.Account;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;

public interface AccountService {

	/**
	 * Мотод добавления пользователя
	 * @param name имя пользователя
	 * @return сущность пользователя
	 * @throws AccountNameRequiredException при указании пустого имени
	 */
	Account addAccount(String name) throws AccountNameRequiredException;

	/**
	 * Метод удаления пользовтеля
	 * @param number номер счета
	 * @throws AccountNotFoundException при указании пустого имени
	 */
	void removeAccount(UUID number) throws AccountNotFoundException;

	/**
	 * Метод получения всех пользователей системы
	 * @return коллекция сущностей пользователей
	 */
	Collection<Account> allAccounts();
}

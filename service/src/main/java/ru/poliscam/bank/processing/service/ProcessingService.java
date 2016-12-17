package ru.poliscam.bank.processing.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import ru.poliscam.bank.processing.database.model.Payment;
import ru.poliscam.bank.processing.service.exceptions.AccountNotFoundException;
import ru.poliscam.bank.processing.service.exceptions.DestinationAccountRequiredException;
import ru.poliscam.bank.processing.service.exceptions.InsufficientMoneyException;
import ru.poliscam.bank.processing.service.exceptions.UnknownPaymentTypeException;

public interface ProcessingService {

	/**
	 * Получить текущий баланс аккаунта
	 * @param number номер счета
	 * @return текущий баланс
	 * @throws AccountNotFoundException если аккаунт не найден в системе
	 */
	BigDecimal getBalance(UUID number) throws AccountNotFoundException;

	/**
	 * Пополнить счет
	 * @param number номер счета
	 * @param amount сумма пополнения
	 * @return текущий баланс
	 * @throws AccountNotFoundException если аккаунт не найден в системе
	 * @throws DestinationAccountRequiredException не используется
	 * @throws UnknownPaymentTypeException не используется
	 * @throws InsufficientMoneyException не используется
	 */
	BigDecimal addMoney(UUID number, BigDecimal amount) throws AccountNotFoundException,
	                                                           DestinationAccountRequiredException,
	                                                           UnknownPaymentTypeException, InsufficientMoneyException;

	/**
	 * Снять со счета
	 * @param number номер счета
	 * @param amount сумма снятия
	 * @return текущий баланс
	 * @throws AccountNotFoundException если аккаунт не найден в системе
	 * @throws DestinationAccountRequiredException не используется
	 * @throws UnknownPaymentTypeException не используется
	 * @throws InsufficientMoneyException если баланс счета в результате операции будет меньше нуля
	 */
	BigDecimal spentMoney(UUID number, BigDecimal amount) throws AccountNotFoundException, DestinationAccountRequiredException,
	                                                        UnknownPaymentTypeException, InsufficientMoneyException;

	/**
	 * Перевести со счета на другой счет
	 * @param from номер счета отправителя
	 * @param to номер счета получателя
	 * @param amount сумма перевода
	 * @return текущий баланс счета отправителя
	 * @throws AccountNotFoundException если аккаунт не найден в системе
	 * @throws DestinationAccountRequiredException если не указан аккаунт получателя
	 * @throws UnknownPaymentTypeException не используется
	 * @throws InsufficientMoneyException если баланс счета отправителя в результате операции будет меньше нуля
	 */
	BigDecimal transferMoney(UUID from, UUID to, BigDecimal amount) throws AccountNotFoundException,
	                                                                     DestinationAccountRequiredException,
	                                                                     UnknownPaymentTypeException,
	                                                                     InsufficientMoneyException;

	/**
	 * @param number номер счета
	 * @return коллекцию со всеми транзакциями по счету
	 * @throws AccountNotFoundException если аккаунт не найден в системе
	 */
	Collection<Payment> getPayments(UUID number) throws AccountNotFoundException;

}

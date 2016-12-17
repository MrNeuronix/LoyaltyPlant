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

	BigDecimal getBalance(UUID number) throws AccountNotFoundException;

	BigDecimal addMoney(UUID number, BigDecimal amount) throws AccountNotFoundException,
	                                                           DestinationAccountRequiredException,
	                                                           UnknownPaymentTypeException, InsufficientMoneyException;

	BigDecimal spentMoney(UUID number, BigDecimal amount) throws AccountNotFoundException, DestinationAccountRequiredException,
	                                                        UnknownPaymentTypeException, InsufficientMoneyException;

	BigDecimal transferMoney(UUID from, UUID to, BigDecimal amount) throws AccountNotFoundException,
	                                                                     DestinationAccountRequiredException,
	                                                                     UnknownPaymentTypeException,
	                                                                     InsufficientMoneyException;

	Collection<Payment> getPayments(UUID number) throws AccountNotFoundException;

}

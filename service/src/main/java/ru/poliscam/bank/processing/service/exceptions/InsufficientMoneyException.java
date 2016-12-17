package ru.poliscam.bank.processing.service.exceptions;


public class InsufficientMoneyException extends Exception {

	public InsufficientMoneyException() {
		super("Account not found");
	}

}

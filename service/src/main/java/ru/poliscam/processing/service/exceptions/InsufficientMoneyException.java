package ru.poliscam.processing.service.exceptions;


public class InsufficientMoneyException extends Exception {

	public InsufficientMoneyException() {
		super("Account not found");
	}

}

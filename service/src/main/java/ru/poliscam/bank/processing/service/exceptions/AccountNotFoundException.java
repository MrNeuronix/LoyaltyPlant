package ru.poliscam.bank.processing.service.exceptions;


public class AccountNotFoundException extends Exception {

	public AccountNotFoundException() {
		super("Account not found");
	}

	public AccountNotFoundException(String message) {
		super(message);
	}

}

package ru.poliscam.processing.service.exceptions;


public class AccountNotFoundException extends Exception {

	public AccountNotFoundException() {
		super("Account not found");
	}

}

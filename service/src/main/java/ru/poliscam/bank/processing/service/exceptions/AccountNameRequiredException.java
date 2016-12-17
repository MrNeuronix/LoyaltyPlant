package ru.poliscam.bank.processing.service.exceptions;


public class AccountNameRequiredException extends Exception {

	public AccountNameRequiredException() {
		super("Account name is required");
	}

}

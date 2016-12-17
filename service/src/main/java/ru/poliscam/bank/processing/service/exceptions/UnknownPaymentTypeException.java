package ru.poliscam.bank.processing.service.exceptions;


public class UnknownPaymentTypeException extends Exception {

	public UnknownPaymentTypeException() {
		super("Unknown payment type specified");
	}

}

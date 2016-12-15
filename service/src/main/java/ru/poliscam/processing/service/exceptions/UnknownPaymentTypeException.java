package ru.poliscam.processing.service.exceptions;


public class UnknownPaymentTypeException extends Exception {

	public UnknownPaymentTypeException() {
		super("Unknown payment type specified");
	}

}

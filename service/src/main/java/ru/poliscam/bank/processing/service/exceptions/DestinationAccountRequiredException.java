package ru.poliscam.bank.processing.service.exceptions;


public class DestinationAccountRequiredException extends Exception {

	public DestinationAccountRequiredException() {
		super("Destination account required");
	}

}

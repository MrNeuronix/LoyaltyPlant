package ru.poliscam.processing.service.exceptions;


public class DestinationAccountRequiredException extends Exception {

	public DestinationAccountRequiredException() {
		super("Destination account required");
	}

}

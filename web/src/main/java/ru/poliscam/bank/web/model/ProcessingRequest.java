package ru.poliscam.bank.web.model;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessingRequest {

	private UUID number;
	private UUID to;
	private BigDecimal amount;

	public UUID getNumber() {
		return number;
	}

	public void setNumber(UUID number) {
		this.number = number;
	}

	public UUID getTo() {
		return to;
	}

	public void setTo(UUID to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}

package ru.poliscam.processing.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

	// UUID выступает одновременно и как номер счета и как ID
	@GeneratedValue
	@Column(name = "number", columnDefinition = "CHAR(36)", unique = true)
	@Type(type="uuid-char")
	@Id
	private UUID number;

	// Имя владельца счета
	private String name;

	// Текущий баланс
	@Column(name = "balance", precision = 19, scale = 2, nullable = false)
	private BigDecimal balance;

	// Платежи и переводы с этого аккаунта
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
	@BatchSize(size = 100)
	@OrderBy("date DESC")
	@JsonIgnore
	private Set<Payment> payments = new HashSet<>();

	public Account() {
	}

	public Account(String name) {
		this.name = name;
		this.balance = BigDecimal.ZERO;
	}

	public UUID getNumber() {
		return number;
	}

	public void setNumber(UUID number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@Override
	public String toString() {
		return "Account{" +
		       "number=" + number +
		       ", name='" + name + '\'' +
		       ", balance='" + balance + '\'' +
		       '}';
	}
}
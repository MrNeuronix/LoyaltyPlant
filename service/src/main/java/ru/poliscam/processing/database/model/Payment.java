package ru.poliscam.processing.database.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.poliscam.processing.database.enums.PaymentType;

@Entity
@Table(name = "payments")
public class Payment {

	@GeneratedValue
	@Id
	private long id;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	// Аккаунт платежа
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_number")
	private Account account;

	// Тип изменения баланса
	@Enumerated(value = EnumType.STRING)
	private PaymentType type = PaymentType.UNKNOWN;

	// Если тип == TRANSFER, то тут указывается UUID аккаунта, куда/откуда ушли/пришли деньги
	@Column(name = "otherSide", columnDefinition = "CHAR(36)")
	@Type(type="uuid-char")
	private UUID otherSide;

	// Изменение баланса
	@Column(name = "balancechange", precision = 19, scale = 2, nullable = false)
	private BigDecimal change;

	public Payment() {
	}

	public Payment(Account account, PaymentType type, Account otherSide, BigDecimal change) {
		this.account = account;
		this.type = type;
		this.otherSide = otherSide == null ? null : otherSide.getNumber();
		this.change = change;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public UUID getOtherSide() {
		return otherSide;
	}

	public void setOtherSide(UUID otherSide) {
		this.otherSide = otherSide;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Payment payment = (Payment) o;

		if (id != payment.id)
			return false;
		if (date != null ? !date.equals(payment.date) : payment.date != null)
			return false;
		if (account != null ? !account.equals(payment.account) : payment.account != null)
			return false;
		if (type != payment.type)
			return false;
		if (otherSide != null ? !otherSide.equals(payment.otherSide) : payment.otherSide != null)
			return false;
		return change != null ? change.equals(payment.change) : payment.change == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (account != null ? account.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (otherSide != null ? otherSide.hashCode() : 0);
		result = 31 * result + (change != null ? change.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Payment{" +
		       "id=" + id +
		       ", date=" + date +
		       ", account=" + account +
		       ", type=" + type +
		       ", change=" + change +
		       '}';
	}
}
package ru.poliscam.bank.processing.database.dao;

import org.springframework.data.repository.CrudRepository;

import ru.poliscam.bank.processing.database.model.Payment;

public interface PaymentDAO extends CrudRepository<Payment, Long> {
}
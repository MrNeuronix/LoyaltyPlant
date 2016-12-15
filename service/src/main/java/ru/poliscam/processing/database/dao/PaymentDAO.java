package ru.poliscam.processing.database.dao;

import org.springframework.data.repository.CrudRepository;

import ru.poliscam.processing.database.model.Payment;

public interface PaymentDAO extends CrudRepository<Payment, Long> {
}
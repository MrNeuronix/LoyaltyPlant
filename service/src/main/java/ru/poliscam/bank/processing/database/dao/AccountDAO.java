package ru.poliscam.bank.processing.database.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import ru.poliscam.bank.processing.database.model.Account;

public interface AccountDAO extends CrudRepository<Account, UUID> {
	Account findByNumber(UUID number);
}
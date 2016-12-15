package ru.poliscam.processing.database.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import ru.poliscam.processing.database.model.Account;

public interface AccountDAO extends CrudRepository<Account, UUID> {
	Account findByNumber(UUID number);
}
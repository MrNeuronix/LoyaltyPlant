package ru.poliscam.bank.processing.service;

import java.util.Collection;
import java.util.UUID;

public interface LocksService {

	/**
	 * Получить блокировку на запись
	 * @param number номер счета
	 */
	void writeLock(UUID number);

	/**
	 * Освободить блокировку на запись
	 * @param number номер счета
	 */
	void writeUnlock(UUID  number);

	/**
	 * Получить блокировку на чтение
	 * @param number номер счета
	 */
	void readLock(UUID number);

	/**
	 * Освободить блокировку на чтение
	 * @param number номер счета
	 */
	void readUnlock(UUID number);

	/**
	 * Получить блокировку на несколько ресурсов одновременно
	 * Реализация позволяет измежать deadlock
	 * @param number коллекция номеров счетов
	 */
	void bulkWriteLock(Collection<UUID> number);

	/**
	 * Освободить блокировки на несколько ресурсов одновременно
	 * Реализация позволяет измежать deadlock
	 * @param number коллекция номеров счетов
	 */
	void bulkWriteUnlock(Collection<UUID> number);
}

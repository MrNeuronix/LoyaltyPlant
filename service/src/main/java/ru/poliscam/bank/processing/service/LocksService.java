package ru.poliscam.bank.processing.service;

import java.util.Collection;
import java.util.UUID;

public interface LocksService {
	void writeLock(UUID number);
	void writeUnlock(UUID  number);
	void readLock(UUID number);
	void readUnlock(UUID number);

	void bulkWriteLock(Collection<UUID> number);
	void bulkWriteUnlock(Collection<UUID> number);
}

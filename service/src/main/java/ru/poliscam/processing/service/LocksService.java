package ru.poliscam.processing.service;

import java.util.UUID;

public interface LocksService {
	void writeLock(UUID number);
	void writeUnlock(UUID  number);
	void readLock(UUID number);
	void readUnlock(UUID number);
}

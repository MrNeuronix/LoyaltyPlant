package ru.poliscam.processing.service;

import com.google.common.util.concurrent.Striped;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;

@Service
public class LocksServiceImpl implements LocksService {

	// Т.к. блокировки на уровне базе запрещены по условию,
	// реализуем этот функционал на уровне приложения.
	// 128 - минимальное кол-во локов
	private final Striped<ReadWriteLock> locks = Striped.lazyWeakReadWriteLock(128);

	@Override
	public void writeLock(UUID number) {
		locks.get(number).writeLock().lock();
	}

	@Override
	public void writeUnlock(UUID number) {
		locks.get(number).writeLock().unlock();
	}

	@Override
	public void readLock(UUID number) {
		locks.get(number).readLock().lock();
	}

	@Override
	public void readUnlock(UUID number) {
		locks.get(number).readLock().lock();
	}
}

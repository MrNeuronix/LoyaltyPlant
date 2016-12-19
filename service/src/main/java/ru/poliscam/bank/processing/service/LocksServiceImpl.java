package ru.poliscam.bank.processing.service;

import com.google.common.util.concurrent.Striped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;

@Service
public class LocksServiceImpl implements LocksService {

	// Т.к. блокировки на уровне базе запрещены по условию,
	// реализуем этот функционал на уровне приложения.
	// Блокировки - lazy weak, поэтому могут быть собраны GC, когда будут не нужны
	// 256 stripes выбрано с запасом, ожидаемый уровень конкурентных запросов - не более 100
	private final Striped<ReadWriteLock> locks = Striped.lazyWeakReadWriteLock(256);
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		locks.get(number).readLock().unlock();
	}

	@Override
	public void bulkWriteLock(Collection<UUID> numbers) {
		locks.bulkGet(numbers).forEach(lock -> lock.writeLock().lock());
	}

	@Override
	public void bulkWriteUnlock(Collection<UUID> numbers) {
		locks.bulkGet(numbers).forEach(lock -> lock.writeLock().unlock());
	}
}

package ru.poliscam.processing.service;

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
		logger.info("Get write lock on {}", number);
		locks.get(number).writeLock().lock();
		logger.info("Lock on {} getted!", number);
	}

	@Override
	public void writeUnlock(UUID number) {
		logger.info("Unlocking {}", number);
		locks.get(number).writeLock().unlock();
		logger.info("Lock on {} released!", number);
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
		logger.info("Get write lock on {}", numbers);
		locks.bulkGet(numbers).forEach(lock -> lock.writeLock().lock());
		logger.info("Lock on {} getted!", numbers);
	}

	@Override
	public void bulkWriteUnlock(Collection<UUID> numbers) {
		logger.info("Get write unlock on {}", numbers);
		locks.bulkGet(numbers).forEach(lock -> lock.writeLock().unlock());
		logger.info("Released write unlock on {}", numbers);
	}
}

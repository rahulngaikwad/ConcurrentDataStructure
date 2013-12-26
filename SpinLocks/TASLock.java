
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


public class TASLock implements Lock{
	AtomicBoolean lock = new AtomicBoolean(false);
	
	@Override
	public void lock() {		
		while(lock.getAndSet(true)){}
	}

	@Override
	public void unlock() {
		lock.set(false);
	}

	@Override
	public Condition newCondition() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean tryLock() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new java.lang.UnsupportedOperationException();
	}
}

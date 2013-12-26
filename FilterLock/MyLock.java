import java.util.concurrent.atomic.AtomicInteger;


public class MyLock implements Lock{

	AtomicInteger lock = new AtomicInteger(0);
	
	@Override
	public void lock() {		
		while(!lock.compareAndSet(0, 1));
	}

	@Override
	public void unlock() {
		lock.decrementAndGet();
	}
}

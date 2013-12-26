
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements Lock {
	
	private AtomicBoolean lock = new AtomicBoolean(false);
	private  int minDealy; 
	private  int maxDealy; 


	public BackoffLock(int minDelay, int maxDelay) {
		this.minDealy = minDelay;
		this.maxDealy = maxDelay;
	}

	@Override
	public void lock() {
		Backoff backoff = new Backoff(minDealy, maxDealy);
		while (true) {
			while (lock.get()) {
			}
			if (!lock.getAndSet(true)) {
				return;
			} else {
				backoff.backoff();
			}
		}
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
	
	 class Backoff {
		  final int minDelay, maxDelay;
		  int limit;          
		  final Random random; 
		  

		  public Backoff(int min, int max) {
		    if (max < min) {
		      throw new IllegalArgumentException("max must be greater than min");
		    }
		    minDelay = min;
		    maxDelay = min;
		    limit = minDelay;
		    random = new Random();
		  }
		  
		  public void backoff() {
		    int delay = random.nextInt(limit);
		    if (limit < maxDelay) { // double limit if less than max
		    	limit = 2 * limit;
		        limit = limit > maxDelay ? maxDelay : limit;
		    }
		    
		    try {
				Thread.sleep(delay);
			} catch (InterruptedException e) { }
		  }
		}
}

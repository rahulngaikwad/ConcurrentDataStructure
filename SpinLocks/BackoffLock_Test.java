import java.util.concurrent.locks.Lock;

import junit.framework.*;

public class BackoffLock_Test extends TestCase {
	private final static int NO_OF_THREADS = 5;
	private final static int LOCK_ATTEMPTS = 100;
	Thread[] thread = new Thread[NO_OF_THREADS];
	
	Lock lock;

	public BackoffLock_Test(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(BackoffLock_Test.class);
		return suite;
	}

	public void testParallel() throws Exception {	
		lock = new BackoffLock(4,200);
		testLockImplementation(lock);
	}

	private void testLockImplementation(Lock lock) throws Exception {
		ThreadID.reset();
		ConcurrentThread.csCounter = 0;
		
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i] = new ConcurrentThread(lock,LOCK_ATTEMPTS);
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].start();
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].join();
		}
		
		assertEquals(ConcurrentThread.csCounter, NO_OF_THREADS*LOCK_ATTEMPTS);
	}

}


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LazyListTest extends TestCase {
	private static int NO_OF_THREADS = 75;
	private static int NO_OF_ATTEMPTS = 100000;
	private static int VALUE_BOUND = 100;
	static AtomicInteger insertCount = new AtomicInteger();
	static AtomicInteger removeCount = new AtomicInteger();
	
	Thread[] thread = new Thread[NO_OF_THREADS];
	
	public LazyListTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LazyListTest.class);
		return suite;
	}

	public void testParallel() throws Exception {	
		LazyList<Integer> list = new LazyList<Integer>();
		testRemoveFailsOnEmtyList(list);
		
		testSingleInsertRemoveWorks(new LazyList<Integer>());
		
		testListIsSorted(new LazyList<Integer>());

		testInsertRemoveCountMatches(new LazyList<Integer>());
		
	}

	private void testSingleInsertRemoveWorks(LazyList<Integer> list) {
		assertTrue(list.add(5));
		assertTrue(list.remove(5));
	}

	private void testRemoveFailsOnEmtyList(LazyList<Integer> list) {	
		assertFalse(list.remove(0));	
	}

	private void testListIsSorted(LazyList<Integer> list) throws Exception {
		ThreadID.reset(); 
		createAndRunThreads(list);
		for(LazyList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			assertTrue(node.key <= node.next.key);
		}
	}
	
	private void testInsertRemoveCountMatches(LazyList<Integer> list) throws Exception {
		ThreadID.reset();
		insertCount.set(0);
	    removeCount.set(0); 
		createAndRunThreads(list);
		
		int listSize = 0;
		for(LazyList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			listSize++;
		}
		
		assertEquals(listSize, insertCount.get() - removeCount.get());
	}

	private void createAndRunThreads(LazyList<Integer> list) throws InterruptedException {
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i] = new myThread(list);
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].start();
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].join();
		}
	}

	
	
	class myThread extends Thread{
		List<Integer> list; 
		 public myThread(LazyList<Integer> list) {
			 this.list = list;
		}

		public void run() {	
			 Random random = new Random();
			for(int i = 0; i < NO_OF_ATTEMPTS; i++){
				int operation = random.nextInt(3);
				
				switch(operation)
				{
					case 0 : if(list.add(random.nextInt(VALUE_BOUND))){
								insertCount.incrementAndGet();
							 }
							 break;
							 
					case 1 : if(list.remove(random.nextInt(VALUE_BOUND))){
								removeCount.incrementAndGet();
							 }
					         break;
					         
					case 2 : list.contains(random.nextInt(VALUE_BOUND));
				}
				
			}
	     }
	 }
}

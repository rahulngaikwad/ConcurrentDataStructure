
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FineListTest extends TestCase {
	private static int NO_OF_THREADS = 70;
	private static int NO_OF_ATTEMPTS = 10000;
	private static int VALUE_BOUND = 100;
	static AtomicInteger insertCount = new AtomicInteger();
	static AtomicInteger removeCount = new AtomicInteger();
	
	Thread[] thread = new Thread[NO_OF_THREADS];
	
	public FineListTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(FineListTest.class);
		return suite;
	}

	public void testParallel() throws Exception {	
		FineList<Integer> list = new FineList<Integer>();
		testRemoveFailsOnEmtyList(list);
		
		testSingleInsertRemoveWorks(new FineList<Integer>());
		
		testListIsSorted(new FineList<Integer>());

		testInsertRemoveCountMatches(new FineList<Integer>());
		
	}

	private void testSingleInsertRemoveWorks(FineList<Integer> list) {
		assertTrue(list.add(5));
		assertTrue(list.remove(5));
	}

	private void testRemoveFailsOnEmtyList(FineList<Integer> list) {	
		assertFalse(list.remove(0));	
	}

	private void testListIsSorted(FineList<Integer> list) throws Exception {
		ThreadID.reset(); 
		createAndRunThreads(list);
		for(FineList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			assertTrue(node.key <= node.next.key);
		}
	}
	
	private void testInsertRemoveCountMatches(FineList<Integer> list) throws Exception {
		ThreadID.reset();
		insertCount.set(0);
	    removeCount.set(0); 
		createAndRunThreads(list);
		
		int listSize = 0;
		for(FineList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			listSize++;
		}
		
		assertEquals(listSize, insertCount.get() - removeCount.get());
	}

	private void createAndRunThreads(FineList<Integer> list) throws InterruptedException {
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
		 public myThread(FineList<Integer> list) {
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

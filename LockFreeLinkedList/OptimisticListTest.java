
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OptimisticListTest extends TestCase {
	private static int NO_OF_THREADS = 50;
	private static int NO_OF_ATTEMPTS = 1000;
	private static int VALUE_BOUND = 100;
	static AtomicInteger insertCount = new AtomicInteger();
	static AtomicInteger removeCount = new AtomicInteger();
	
	Thread[] thread = new Thread[NO_OF_THREADS];
	
	public OptimisticListTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(OptimisticListTest.class);
		return suite;
	}

	public void testParallel() throws Exception {	
		OptimisticList<Integer> list = new OptimisticList<Integer>();
		testRemoveFailsOnEmtyList(list);
		
		testSingleInsertRemoveWorks(new OptimisticList<Integer>());
		
		testListIsSorted(new OptimisticList<Integer>());

		testInsertRemoveCountMatches(new OptimisticList<Integer>());
		
	}

	private void testSingleInsertRemoveWorks(OptimisticList<Integer> list) {
		assertTrue(list.add(5));
		assertTrue(list.remove(5));
	}

	private void testRemoveFailsOnEmtyList(OptimisticList<Integer> list) {	
		assertFalse(list.remove(0));	
	}

	private void testListIsSorted(OptimisticList<Integer> list) throws Exception {
		ThreadID.reset(); 
		createAndRunThreads(list);
		for(OptimisticList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			//System.out.print(node.key + " ");
			assertTrue(node.key <= node.next.key);
		}
	}
	
	private void testInsertRemoveCountMatches(OptimisticList<Integer> list) throws Exception {
		ThreadID.reset();
		insertCount.set(0);
	    removeCount.set(0); 
		createAndRunThreads(list);
		
		int listSize = 0;
		for(OptimisticList<Integer>.Node node = list.head.next; node.key != Integer.MAX_VALUE; node = node.next){
			listSize++;
		}
		
		assertEquals(listSize, insertCount.get() - removeCount.get());
	}

	private void createAndRunThreads(OptimisticList<Integer> list) throws InterruptedException {
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
		 public myThread(OptimisticList<Integer> list) {
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

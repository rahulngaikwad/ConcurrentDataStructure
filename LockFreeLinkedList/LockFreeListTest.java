
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LockFreeListTest extends TestCase {
	private static int NO_OF_THREADS = 15;
	private static int NO_OF_ATTEMPTS = 1000;
	private static int VALUE_BOUND = 10000;
	static AtomicInteger insertCount = new AtomicInteger();
	static AtomicInteger removeCount = new AtomicInteger();
	
	Thread[] thread = new Thread[NO_OF_THREADS];
	
	public LockFreeListTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LockFreeListTest.class);
		return suite;
	}

	public void testParallel() throws Exception {	
		LockFreeList<Integer> list = new LockFreeList<Integer>();
		testRemoveFailsOnEmtyList(list);
		
		testSingleInsertRemoveWorks(new LockFreeList<Integer>());
		
		testListIsSorted(new LockFreeList<Integer>());

		testInsertRemoveCountMatches(new LockFreeList<Integer>());
		
	}

	private void testSingleInsertRemoveWorks(LockFreeList<Integer> list) {
		assertTrue(list.add(5));
		assertTrue(list.remove(5));
	}

	private void testRemoveFailsOnEmtyList(LockFreeList<Integer> list) {	
		assertFalse(list.remove(0));	
	}

	private void testListIsSorted(LockFreeList<Integer> list) throws Exception {
		ThreadID.reset(); 
		createAndRunThreads(list);
		for(AtomicMarkableReference<LockFreeList<Integer>.Node> node = list.head.next; node.getReference().key != Integer.MAX_VALUE; node = node.getReference().next){
			assertTrue(node.getReference().key <= node.getReference().next.getReference().key);
		}
	}
	
	private void testInsertRemoveCountMatches(LockFreeList<Integer> list) throws Exception {
		ThreadID.reset();
		insertCount.set(0);
	    removeCount.set(0); 
		createAndRunThreads(list);
		int listSize = 0;
		AtomicMarkableReference<LockFreeList<Integer>.Node> pred=null;
		for(AtomicMarkableReference<LockFreeList<Integer>.Node> node = list.head.next; node.getReference().key != Integer.MAX_VALUE; node = node.getReference().next){
			//System.out.print(node.getReference().key + "->");
			System.out.print(node.getReference().key + "->");
			listSize++;
			
		}
		
			
		
		assertEquals(listSize, insertCount.get() - removeCount.get());
	}

	
	
	
	private void createAndRunThreads(LockFreeList<Integer> list) throws InterruptedException {
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
		 public myThread(LockFreeList<Integer> list) {
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

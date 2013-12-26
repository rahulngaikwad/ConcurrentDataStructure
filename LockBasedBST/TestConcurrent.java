import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestConcurrent {

	private static int NO_OF_THREADS;
	private static int NO_OF_ATTEMPTS;
	private static int VALUE_BOUND;
	static AtomicInteger insertCounter, deleteCounter;
	
	BinarySearchTree<Integer> bst;
	
	public TestConcurrent(BinarySearchTree<Integer> bst, int noOfThreads, int noOfOperations, int valueBound) {
		this.bst = bst;
		NO_OF_THREADS = noOfThreads;
		NO_OF_ATTEMPTS = noOfOperations;
		VALUE_BOUND = valueBound;
		insertCounter = new AtomicInteger();
		deleteCounter = new AtomicInteger();
	}

	public void runTest() throws InterruptedException {
		Thread[] thread = new Thread[NO_OF_THREADS];
		
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i] = new myThread(bst);
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].start();
		}
		for (int i = 0; i < NO_OF_THREADS; i++) {
			thread[i].join();
		}
		System.out.println("\n ******************************************\n");
		System.out.println("External Keys : " + bst.printTree());
		System.out.println("\n ******************************************\n");
		System.out.println("Insert Counter : " + insertCounter.get());
		System.out.println("Delete Counter : " + deleteCounter.get());
		System.out.println("Size Counter   : " + bst.getSize());
		System.out.println("External node count : " + bst.countExternalNodes());
		if(bst.getSize() == insertCounter.get() - deleteCounter.get() && bst.countExternalNodes() == bst.getSize())
			System.out.println("Test Successfull");
		else
			System.out.println("Test Failed");
		
	}
	

	static class myThread extends Thread {
		BinarySearchTree<Integer> bst;
		Random random = new Random();
		
		public myThread(BinarySearchTree<Integer> bst) {
			this.bst = bst;
		}

		public void run() {
			
			for (int i = 0; i < NO_OF_ATTEMPTS; i++) {
				int operation = random.nextInt(10);
				int value = random.nextInt(VALUE_BOUND);
				switch (operation) {
				case 0:	
					insertItem( value);
					break;

				case 1:
					deleteItem(value);
					break;

				default: 
					findItem( value);
					break;
				}
			}
		}

	private void findItem(int value) {
		while (true) {
			try {
				if(bst.contains(value)){
					System.out.println("Found : " + value );
				} else { 
					System.out.println("Find Failed : " + value + "  item not present");
				}
				break;
			} catch (IllegalStateException e) { 
				System.out.println("Find failed for : " + value + " due to concurrent modification, retrying...");
			}
		}
	}
	
	private void insertItem(int value) {
		while (true) {
			try {
				if(bst.add(value)){
					insertCounter.getAndIncrement();
					System.out.println("Inserted : " + value );
				} else { 
					System.out.println("Insert Failed : " + value + "  item already present");
				}
				break;
			} catch (IllegalStateException e) { 
				System.out.println("Inserte failed for : " + value + " due to concurrent modification, retrying...");
			}
		}
	}
	
	private void deleteItem(int value) {
		while (true) {
			try {
				if(bst.remove(value)){
					deleteCounter.getAndIncrement();
					System.out.println("Deleted : " + value );
				} else { 
					System.out.println("Deletion failed for : " + value + "  item not present");
				}
				break;
			} catch (IllegalStateException e) { 
				System.out.println("Deletion failed for : " + value + " due to concurrent modification, retrying...");
			}
		}
	}
	}
}

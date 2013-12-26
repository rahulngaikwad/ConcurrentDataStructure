
import java.util.Random;


public class ConcurrentThread extends Thread{
	 
	private final int noOfAttempts;
	private final BinarySearchTree<Integer> bst;
	public static int csCounter;
	private final int valueBound;
	Random random;
	
	public ConcurrentThread(BinarySearchTree<Integer> bst,int noOfAttempts, int valueBound){
		 this.bst = bst;
		 this.noOfAttempts = noOfAttempts;
		 this.valueBound = valueBound;
		 this.random  = new Random();
	 }
	 
	 public void run() {	
		for(int i = 0; i < noOfAttempts; i++){
			int operation = random.nextInt(10);
			int value = random.nextInt(valueBound);
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
				//System.out.println("Found : " + value );
			} else { 
				//System.out.println("Find Failed : " + value + "  item not present");
			}
			break;
		} catch (IllegalStateException e) { 
			//System.out.println("Find failed for : " + value + " due to concurrent modification, retrying...");
		}
	}
}

private void insertItem(int value) {
	while (true) {
		try {
			if(bst.add(value)){
				//System.out.println("Inserted : " + value );
			} else { 
				//System.out.println("Insert Failed : " + value + "  item already present");
			}
			break;
		} catch (IllegalStateException e) { 
			//System.out.println("Inserte failed for : " + value + " due to concurrent modification, retrying...");
		}
	}
}

private void deleteItem(int value) {
	while (true) {
		try {
			if(bst.remove(value)){
				//System.out.println("Deleted : " + value );
			} else { 
				//System.out.println("Deletion failed for : " + value + "  item not present");
			}
			break;
		} catch (IllegalStateException e) { 
			//System.out.println("Deletion failed for : " + value + " due to concurrent modification, retrying...");
		}
	}
}
 }
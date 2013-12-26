

public class TestSequential {
	int insertCounter, deleteCounter;
	BinarySearchTree<Integer> bst;
	
	
	public TestSequential(BinarySearchTree<Integer> bst) {
		this.bst = bst;
	}

	public void runTest() {
		
		insertMultipleItems(12, 10, 200, 10);
		System.out.println("Insert Finished. current size = " + bst.getSize());
		showTree();
		insertCounter = bst.getSize();
		
		findMultipleItems(12, 0, 210, 10);
		System.out.println("Find Finished. current size = " + bst.getSize());
		showTree();
		
		deleteMultipleItems(12, 0, 210, 10);
		System.out.println("Delete Finished. current size = " + bst.getSize());
		showTree();
		
		findMultipleItems(10, 10, 200, 10);
		System.out.println("Find Finished. current size = " + bst.getSize());
		showTree();
		
		if(insertCounter == 20 && bst.getSize() == 0)
			System.out.println("Test Successfull");
		else
			System.out.println("Test Failed");
	}


	private void showTree() {
		System.out.println("\n ******************************************\n");
		System.out.println("External Keys : " + bst.printTree());
		System.out.println("\n ******************************************\n");	
	}


	private void findMultipleItems(int noOfItems, int smallest, int largest, int increament ) {	
		for(int i = 0 ; i < noOfItems ; i++)
		{		
			int samllValue = smallest + i*increament;
			findItem(samllValue);
			
			int largeValue = largest - i*increament;
			findItem(largeValue);
		}
	}
	
	
	private void insertMultipleItems(int noOfItems, int smallest, int largest, int increament ) {	
		for(int i = 0 ; i < noOfItems ; i++)
		{		
			int samllValue = smallest + i*increament;
			insertItem(samllValue);
			
			int largeValue = largest - i*increament;
			insertItem(largeValue);
		}
	}
	
	private void deleteMultipleItems(int noOfItems, int smallest, int largest, int increament ) {	
		for(int i = 0 ; i < noOfItems ; i++)
		{		
			int samllValue = smallest + i*increament;
			deleteItem(samllValue);
			
			int largeValue = largest - i*increament;
			deleteItem(largeValue);
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

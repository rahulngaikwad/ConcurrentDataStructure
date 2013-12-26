
public class OptimisticBSTTestConcurrent {

	private static int NO_OF_THREADS = 50;
	private static int NO_OF_ATTEMPTS = 10000;
	private static int VALUE_BOUND = 50;

	public static void main(String[] args) throws InterruptedException {
		BinarySearchTree<Integer> bst = new OptimisticBST<>();
		new TestConcurrent(bst, NO_OF_THREADS,NO_OF_ATTEMPTS,VALUE_BOUND).runTest();
	}
}

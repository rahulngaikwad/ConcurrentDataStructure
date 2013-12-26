
public class OptimisticBSTTestSequential {
	public static void main(String[] args) {
		BinarySearchTree<Integer> bst = new OptimisticBST<>();
		new TestSequential(bst).runTest();
	}
}

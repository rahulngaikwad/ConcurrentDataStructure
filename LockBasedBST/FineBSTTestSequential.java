
public class FineBSTTestSequential {
	public static void main(String[] args) {
		BinarySearchTree<Integer> bst = new FineBST<>();
		new TestSequential(bst).runTest();
	}
}

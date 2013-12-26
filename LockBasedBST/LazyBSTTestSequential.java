
public class LazyBSTTestSequential {
	public static void main(String[] args) {
		BinarySearchTree<Integer> bst = new LazyBST<>();
		new TestSequential(bst).runTest();
	}
}


public class CourseBSTTestSequential {
	public static void main(String[] args) {
		BinarySearchTree<Integer> bst = new CoarseBST<>();
		new TestSequential(bst).runTest();
	}
}

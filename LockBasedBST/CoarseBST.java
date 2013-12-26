
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class CoarseBST<Type> extends BinarySearchTree<Type> {

	private Node root;
	private Lock lock;

	public CoarseBST() {
		// SENTINAL NODES
		root = new Node(0);
		root.leftChild = new Node(Integer.MIN_VALUE);
		root.rightChild = new Node(Integer.MAX_VALUE);
		size = new AtomicInteger(0);
		lock = new ReentrantLock();
	}

	/**
	 * Acquires lock
	 * Locate parent and curr i.e. internal and external nodes position for insert.
	 * If key already present, then do not insert, return 
	 * else create new external node with given data.
	 * create new internal node with key = max(key,curr.key)
	 * Attach curr and new external node to newly created internal node
	 * Insert internal node as a parents child
	 * Release lock
	 * @param item : item to insert
	 * @return true if inserted successfully
	 */
	@Override
	public boolean add(Type item) {
		Node parent, curr;
		int key = item.hashCode();
		lock.lock();
		try {
			parent = root;
			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			while (curr.leftChild != null && curr.rightChild != null) {
				parent = curr;
				curr = key < curr.key ? curr.leftChild : curr.rightChild;
			}

			if (key == curr.key) { //if already exist return
				return false;
			}

			//create new leaf/external node
			Node node = new Node(item);
			Node internalNode;

			//create new internal node and attach new leaf and curr to it
			if (key > curr.key) {
				internalNode = new Node(key);
				internalNode.rightChild = node;
				internalNode.leftChild = curr;
			} else {
				internalNode = new Node(curr.key);
				internalNode.leftChild = node;
				internalNode.rightChild = curr;
			}

			//insert new internal node as child of parent node
			if (parent.leftChild == curr)
				parent.leftChild = internalNode;
			else
				parent.rightChild = internalNode;

			//increment size by one
			size.incrementAndGet();
			return true;

		} finally {
			lock.unlock();
		}
	}

	/**
	 * Acquires lock
	 * Locate grandParent, parent and curr for given item.
	 * If kye != curr.key , then item is not present, return false
	 * else remove item by attaching curr nodes sibling to grandparent. 
	 * Release lock
	 * @param item : item to remove
	 * @return true if removed successfully
	 */
	@Override
	public boolean remove(Type item) {
		Node curr, parent, grandParent = null;
		int key = item.hashCode();
		lock.lock();
		try {
			parent = root;
			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			while (curr.leftChild != null && curr.rightChild != null) {
				grandParent = parent;
				parent = curr;
				curr = key < curr.key ? curr.leftChild : curr.rightChild;
			}

			if (grandParent == null) {
				return false; // can not delete SENTINAL nodes
			}

			if (key != curr.key) {
				return false; // not present
			}

			// present
			Node sibling = parent.leftChild == curr ? parent.rightChild : parent.leftChild;
			if (grandParent.leftChild == parent)
				grandParent.leftChild = sibling;
			else
				grandParent.rightChild = sibling;

			size.decrementAndGet();
			return true;
		} finally { // always unlock
			lock.unlock();
		}
	}

	/**
	 * Works same way like add/remove
	 * Acquires lock
	 * Traverse the tree and finds the leaf node i.e. curr based on the key
	 * Returns true if curr.key equals to the key we are looking for
	 * Release lock
	 * @param item : item to find
	 * @return true if item found.
	 */
	@Override
	public boolean contains(Type item) {
		Node curr, parent, grandParent = null;
		int key = item.hashCode();
		lock.lock();
		try {
			parent = root;
			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			while (curr.leftChild != null && curr.rightChild != null) {
				grandParent = parent;
				parent = curr;
				curr = key < curr.key ? curr.leftChild : curr.rightChild;
			}

			if (grandParent == null) {
				return false; // can not delete SENTINAL nodes
			}

			return key == curr.key;

		} finally { // always unlock
			lock.unlock();
		}
	}

	 class Node {	
			Type item;
			int key;
			Node leftChild;
			Node rightChild;
			
		Node(Type item) {
			this.item = item;
			this.key = item.hashCode();
		}

		Node(int key) {
			this.item = null;
			this.key = key;
		}
	}

	@Override
	public int countExternalNodes() {
		return countExternalNodes(root.rightChild.leftChild) + countExternalNodes(root.leftChild.rightChild);
	}

	private int countExternalNodes(Node curr) {
		if(curr == null)
			return 0;	
		if(curr.leftChild == null && curr.rightChild == null)
			return 1;
		return countExternalNodes(curr.leftChild) + countExternalNodes(curr.rightChild);	
	}

	@Override
	public String printTree() {
		StringBuffer stringBuffer = new StringBuffer();
		printTree(root.rightChild.leftChild,stringBuffer);
		printTree(root.leftChild.rightChild,stringBuffer);
		return stringBuffer.toString();	
	}
	
	private void printTree(Node curr, StringBuffer stringBuffer) {
		if(curr == null)
			return;	
		if(curr.leftChild == null && curr.rightChild == null)
			stringBuffer.append(curr.item + ",");
		else{ 
			printTree(curr.leftChild, stringBuffer);
			printTree(curr.rightChild, stringBuffer);	
		}
	}
	
}

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyBST<Type> extends BinarySearchTree<Type> {

	private Node root;
	public LazyBST() {
		// SENTINAL NODES
		root = new Node(0);
		root.leftChild = new Node(Integer.MIN_VALUE);
		root.rightChild = new Node(Integer.MAX_VALUE);
		size = new AtomicInteger(0);
	}

	/**
	 * Validation does not retraverse the entire list 
	 * validate checks that curr, parent, grandparent node has not been logically deleted i.e. marked, 
	 * and that parent still points to curr and grandparent still points to parent.
	 * @param grandParent
	 * @param parent
	 * @param curr
	 * @return true if validate successfully
	 */
	private boolean validate(Node grandParent, Node parent, Node curr) {
		
		//both parent and curr are not marked and parent still points to curr
		boolean condition1 = !curr.marked && !parent.marked && curr == (curr.key < parent.key ? parent.leftChild : parent.rightChild);
		if (grandParent == null) {
			return condition1;
		}
		
		//grandparent not marked and still points to parent 
		boolean condition2 = !grandParent.marked && parent == (parent.key < grandParent.key ? grandParent.leftChild : grandParent.rightChild);

		return condition1 && condition2;
	}
	
	/**
	 * Ignore locks, Locate grandParent, parent and curr i.e. internal and external nodes for insert.
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree, points to correct successor and unmarked, if not throw IllegalStateException. validation does not retraverse the entire list 
	 * If key is already present at leaf level, then do not insert, return false
	 * else create new external/leaf node with given data and unmarked it.
	 * create new internal node with key = max(item.key,curr.key) and and unmarked it.
	 * Attach curr and new external/leaf node to newly created internal node
	 * Insert newly created internal node as a child of parent.
	 * Release locks
	 * @param item : item to insert
	 * @return true if inserted successfully
	 */
	@Override
	public boolean add(Type item) {
		int key = item.hashCode();
		Node curr, parent, grandParent = null;
		parent = root;
		curr = key < parent.key ? parent.leftChild : parent.rightChild;
		while (curr.leftChild != null && curr.rightChild != null) {
			grandParent = parent;
			parent = curr;
			curr = key < curr.key ? curr.leftChild : curr.rightChild;
		}
		
		if (grandParent != null)
			grandParent.lock();
		
		try {
			parent.lock();
			try {
				curr.lock();
				try {
					 // validate that all locked nodes are still part of the tree and unchanged.
					if (!validate(grandParent, parent, curr)) {
						throw new IllegalStateException();
					}

					if (key == curr.key) { // already present;
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
					curr.unlock();
				}
			} finally {
				parent.unlock();
			}
		} finally {
			if (grandParent != null)
				grandParent.unlock();
		}
	}

	/**
	 * The remove method is lazy, taking two steps: 
	 * first, mark the target leaf node and its parent, logically removing it, 
	 * and second, redirect its grandparents left/right child field to nodes sibling, physically removing it.
	 *
	 * Ignore locks, Locate grandParent, parent and curr, where curr is external/leaf node
	 * Acquire locks for grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree, points to correct successor and unmarked, if not throw IllegalStateException. validation does not retraverse the entire list 
	 * Remove node in above mentioned two steps. 
	 * Release locks in any case
	 * @param item : item to remove
	 * @return true if removed successfully
	 */
	@Override
	public boolean remove(Type item) {
		int key = item.hashCode();
		Node curr, parent, grandParent = null;
		parent = root;
		
		curr = key < parent.key ? parent.leftChild : parent.rightChild;
		while (curr.leftChild != null && curr.rightChild != null) {
			grandParent = parent;
			parent = curr;
			curr = key < curr.key ? curr.leftChild : curr.rightChild;
		}

		if (grandParent == null)
			return false; // Empty tree => can not delete SENTINAL nodes
		
		grandParent.lock();
		try {
			parent.lock();
			try {
				curr.lock();
				try {

					if (!validate(grandParent, parent, curr)) {
						throw new IllegalStateException();
					}

					if (key != curr.key) {
						return false; // not present
					}
					
					// first logically delete
					curr.marked = true;
					parent.marked = true;
					// then physically delete
					Node sibling = (parent.leftChild == curr ? parent.rightChild : parent.leftChild);
					if (grandParent.leftChild == parent)
						grandParent.leftChild = sibling;
					else
						grandParent.rightChild = sibling;

					size.decrementAndGet();
					return true;

				} finally {
					curr.unlock();
				}
			} finally {
				parent.unlock();
			}
		} finally {
				grandParent.unlock();
		}
	}

	/**
	 * It is wait-free.
	 * If a traversing thread does not find a node, or finds it marked, then that item is not in the tree.
     * Traverses the tree ignoring locks and returns true only if the node it was searching for is present and unmarked.
	 * @param item : item to find
	 * @return true if item found.
	 */
	@Override
	public boolean contains(Type item) {
		int key = item.hashCode();
		Node curr, parent, grandParent = null;
		parent = root;

		curr = key < parent.key ? parent.leftChild : parent.rightChild;
		while (curr.leftChild != null && curr.rightChild != null) {
			grandParent = parent;
			parent = curr;
			curr = key < curr.key ? curr.leftChild : curr.rightChild;
		}

		if (grandParent == null)
			return false; // Tree is empty
		
		return key == curr.key && !curr.marked; //returns true only if we found the key and unmarked
	}

	class Node {
		Type item;
		int key;
		volatile Node leftChild;
		volatile Node rightChild;
		private Lock lock;
		boolean marked;
		 
		Node(Type item) {
			this.item = item;
			this.key = item.hashCode();
			this.marked = false;
			this.lock = new ReentrantLock();
		}

		Node(int key) {
			this.item = null;
			this.key = key;
			this.marked = false;
			this.lock = new ReentrantLock();
		}

		void lock() {
			lock.lock();
		}

		void unlock() {
			lock.unlock();
		}
	}
	
	@Override
	public int countExternalNodes() {
		return countNodes(root.rightChild.leftChild) + countNodes(root.leftChild.rightChild);
	}

	private int countNodes(Node curr) {
		if(curr == null)
			return 0;	
		if(curr.leftChild == null && curr.rightChild == null)
			return 1;
		return countNodes(curr.leftChild) + countNodes(curr.rightChild);	
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

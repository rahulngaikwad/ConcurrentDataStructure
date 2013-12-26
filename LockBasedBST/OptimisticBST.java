import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticBST<Type> extends BinarySearchTree<Type> {

	private Node root;
	public OptimisticBST() {
		// SENTINAL NODES
		root = new Node(0);
		root.leftChild = new Node(Integer.MIN_VALUE);
		root.rightChild = new Node(Integer.MAX_VALUE);
		size = new AtomicInteger(0);
	}

	/**
	 * Validate checks that curr, parent and grandparent node has not been deleted, i.e still part of tree
	 * and that parent still points to curr and grandparent still points to parent.
	 * @param grandParent
	 * @param parent
	 * @param curr
	 * @return true if validate successfully
	 */
	private boolean validate(Node predPred, Node pred, Node next) {
		Node grandParent = null, parent = root;
		Node curr = next.key < parent.key ? parent.leftChild : parent.rightChild;
		while (curr.leftChild != null && curr.rightChild != null) {
			grandParent = parent;
			parent = curr;
			curr = next.key < curr.key ? curr.leftChild : curr.rightChild;
		}

		if (grandParent != predPred || parent != pred || curr != next)
			return false;

		if (grandParent != null)
			if (pred != (next.key < grandParent.key ? grandParent.leftChild : grandParent.rightChild))
				return false;

		if (next != (next.key < parent.key ? parent.leftChild : parent.rightChild))
			return false;

		return true;
	}

	/**
	 * Ignore locks, Locate grandParent, parent and curr i.e. internal and external nodes for insert.
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree and points to correct successor, if not throw IllegalStateException. validation retraverse the entire path 
	 * If item.key is already present at leaf level, then do not insert, return false
	 * else create new external/leaf node with given data.
	 * create new internal node with key = max(item.key,curr.key)
	 * Attach curr and new external/leaf node to newly created internal node
	 * Insert newly created internal node as a child of old parent.
	 * Release locks of grandParent, parent and curr in any case
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
	 * Ignore locks, Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree and points to correct successor, if not throw IllegalStateException. validation retraverse the entire path 
	 * If item.kye != curr.key , then item is not present, can not remove it so return false 
	 * else remove item by redirecting its grandparents left/right child field to nodes sibling.
	 * Release locks of grandParent, parent and curr in any case
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
		
		//Acquires lock
		grandParent.lock();
		try {
			parent.lock();
			try {
				curr.lock();
				try {
					//Validate
					if (!validate(grandParent, parent, curr)) {
						throw new IllegalStateException();
					}

					if (key != curr.key) {
						return false; // item not present
					}
					
					// if item is present remove it along with its parent node by attaching the items sibling directly to the grandparent
					Node sibling = parent.leftChild == curr ? parent.rightChild : parent.leftChild;
					if (grandParent.leftChild == parent)
						grandParent.leftChild = sibling;
					else
						grandParent.rightChild = sibling;

					size.decrementAndGet();
					return true;

				//release locks
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
	 * contains method works same as add/remove.
	 * Ignore locks, Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree and unchanged, if not throw IllegalStateException.
	 * If item.key == curr.key , then item is present, so return true 
	 * return false otherwise
	 * Release locks of grandParent, parent and curr in any case
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
		
		if (key != curr.key) {
			return false; // not present
		}
		
		if (grandParent != null)
			grandParent.lock();
		try {
			parent.lock();
			try {
				curr.lock();
				try {

					if (!validate(grandParent, parent, curr)) {
						throw new IllegalStateException();
					}
	
					return key == curr.key;
					
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

	class Node {
		Type item;
		int key;
		volatile Node leftChild;
		volatile Node rightChild;
		private Lock lock;

		Node(Type item) {
			this.item = item;
			this.key = item.hashCode();
			this.lock = new ReentrantLock();
		}

		Node(int key) {
			this.item = null;
			this.key = key;
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

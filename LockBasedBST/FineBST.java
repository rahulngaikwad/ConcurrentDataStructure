
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineBST<Type> extends BinarySearchTree<Type> {

	private Node root;

	public FineBST() {
		// SENTINAL NODES
		root = new Node(0);
		root.leftChild = new Node(Integer.MIN_VALUE);
		root.rightChild = new Node(Integer.MAX_VALUE);
		size = new AtomicInteger(0);
	}

	/**
	 * Acquires lock by hand-over-hand order
	 * Locate parent and curr i.e. internal and external nodes position for insert.
	 * If item.key is already present at leaf level, then do not insert, return false
	 * else create new external/leaf node with given data.
	 * create new internal node with key = max(item.key,curr.key)
	 * Attach curr and new external/leaf node to newly created internal node
	 * Insert newly created internal node as a child of old parent.
	 * Releases all locks in any case
	 * @param item : item to insert
	 * @return true if inserted successfully
	 */
	@Override
	public boolean add(Type item) {
		Node parent, curr;
		int key = item.hashCode();
		parent = root;
		parent.lock();
		try {
			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			curr.lock();
			try {
				while (curr.leftChild != null && curr.rightChild != null) {
					parent.unlock();
					parent = curr;
					curr = key < curr.key ? curr.leftChild : curr.rightChild;
					curr.lock();  // hand-over-hand locking
				}

				if (key == curr.key) { //already present;
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
	}

	/**
	 * Acquires lock by hand-over-hand order
	 * Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * If item.kye != curr.key , then item is not present, can not remove it so return false 
	 * else remove item by redirecting its grandparents left/right child field to nodes sibling.
	 * Release locks of grandParent, parent and curr in any case
	 * @param item : item to remove
	 * @return true if removed successfully
	 */
	@Override
	public boolean remove(Type item) {
		Node curr, parent, grandParent = null;
		int key = item.hashCode();
		parent = root;
		parent.lock();
		try {
			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			curr.lock();
			try {
				while (curr.leftChild != null && curr.rightChild != null) {
					if (grandParent != null) {
						grandParent.unlock();
					}
					grandParent = parent;
					parent = curr;
					curr = key < curr.key ? curr.leftChild : curr.rightChild;
					curr.lock();
				}

				if (grandParent == null) {
					return false; // can not delete SENTINAL nodes
				}

				if (key != curr.key) {
					return false; // not present
				}

				// if item is present remove it along with its parent node by attaching the items sibling directly to the grandparent
				Node sibling = parent.leftChild == curr ? parent.rightChild : parent.leftChild;
				if (grandParent.leftChild == parent)
					grandParent.leftChild = sibling;
				else
					grandParent.rightChild = sibling;

				//Decrement size
				size.decrementAndGet();
				return true;
			} finally { // always unlock
				curr.unlock();
			}
		} finally { // always unlock
			parent.unlock();
			if (grandParent != null) {
				grandParent.unlock();
			}
		}
	}

	/**
	 * contains do not acquires any lock, it is easy to show that this implementation is linearizable.
	 * Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * If key == curr.key then item of given key is present, return true, otherwise return false
	 * @param item : item to find
	 * @return true if item found.
	 */
	@Override
	public boolean contains(Type item) {
		Node curr, parent, grandParent = null;
		int key = item.hashCode();
		parent = root;
		//parent.lock();

			curr = key < parent.key ? parent.leftChild : parent.rightChild;
			//curr.lock();
			
				while (curr.leftChild != null && curr.rightChild != null) {
					if (grandParent != null) {
						//grandParent.unlock();
					}
					grandParent = parent;
					parent = curr;
					curr = key < curr.key ? curr.leftChild : curr.rightChild;
					//curr.lock();
				}
	
				if (grandParent == null) { //Empty tree i.e. contains only setinal nodes.
					return false;
				}
				
				return key == curr.key;
	}

	class Node {
		Type item;
		int key;
		Node leftChild;
		Node rightChild;
		Lock lock;

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

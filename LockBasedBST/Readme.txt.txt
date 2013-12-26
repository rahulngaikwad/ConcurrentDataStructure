
**************** How to compile and run ******************************
1. Copy all .java files 

2. Compile all files
   e.g javac -cp *.java

3. Open input.txt and supply config parameters i.e. min and max number of threads, number of operation and valu bound
	e.g
	MIN_NO_OF_THREADS=5
	MAX_NO_OF_THREADS=20
	THREAD_INCREAMENT=1
	NO_OF_OPERATIONS=100000
	VALUE_BOUND=500
	
4. run program using MainDriver java class with inputfile name as argument 
   and check output
	e.g
	{cs6360:~/ConDS/prj4} javac *.java
	{cs6360:~/ConDS/prj4} java MainDriver input.txt
	
***********************Analysis **************************************
5. Plrease refer Report for analysis part.

***********************Testing ***************************************
6. Testing : 
i have implemented 4 diffrent BST i.e Course, Fine, Lazy and optimistic.
For each implementation i have written 2 different test files to check sequential and concurrent behaviar.
Please run relevant file in order to test corresponding implementation.

a) Coarse grained implemenations files
 CoarseBST.java
 CourseBSTTestConcurrent.java
 CourseBSTTestSequential.java
 
b) Fine grained implemenation files
 FineBST.java
 FineBSTTestConcurrent.java
 FineBSTTestSequential.java

c) Lazy implemenation files
 LazyBST.java
 LazyBSTTestConcurrent.java
 LazyBSTTestSequential.java

d)Optimistic implemenation files
 OptimisticBST.java
 OptimisticBSTTestConcurrent.java
 OptimisticBSTTestSequential.java

********************description/psudocode ****************************************
6. description/psudocode

1.FineGrained Binary Search Tree
*************************************************
a) add()
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
b) remove()
	 * Acquires lock by hand-over-hand order
	 * Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * If item.kye != curr.key , then item is not present, can not remove it so return false 
	 * else remove item by redirecting its grandparents left/right child field to nodes sibling.
	 * Release locks of grandParent, parent and curr in any case
	 * @param item : item to remove
	 * @return true if removed successfully
c) contains()
	 * contains basically works in the same way as add or remove
	 * Acquires lock by hand-over-hand order
	 * Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * If key == curr.key then item of given key is present, return true, otherwise return false
	  * Release locks of grandParent, parent and curr in any case
	 * @param item : item to find
	 * @return true if item found.
	 
	 
	 
2. Optimistic Binary Search Tree
*************************************************
a) add()
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
b) remove()
	 * Ignore locks, Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree and points to correct successor, if not throw IllegalStateException. validation retraverse the entire path 
	 * If item.kye != curr.key , then item is not present, can not remove it so return false 
	 * else remove item by redirecting its grandparents left/right child field to nodes sibling.
	 * Release locks of grandParent, parent and curr in any case
	 * @param item : item to remove
	 * @return true if removed successfully
c) contains()
	 * contains method works same as add/remove.
	 * Ignore locks, Locate grandParent, parent and curr for given item, where curr is external/leaf node
	 * Acquire locks of grandParent, parent and curr in that order.
	 * Validate that all locked nodes are still part of the tree and unchanged, if not throw IllegalStateException.
	 * If item.key == curr.key , then item is present, so return true 
	 * return false otherwise
	 * Release locks of grandParent, parent and curr in any case
	 * @param item : item to find
	 * @return true if item found.
	



3. Lazy Binary Search Tree
*************************************************
a) add()
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
b) remove()
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

c) contains()
	 * It is wait-free.
	 * If a traversing thread does not find a node, or finds it marked, then that item is not in the tree.
     * Traverses the tree ignoring locks and returns true only if the node it was searching for is present and unmarked.
	 * @param item : item to find
	 * @return true if item found.


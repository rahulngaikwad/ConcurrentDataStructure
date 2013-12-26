
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyList<T>  implements List<T>{
  
   Node head;
  public LazyList() {
    this.head  = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }
  
  private boolean validate(Node pred, Node curr) {
    return  !pred.marked && !curr.marked && pred.next == curr;
  }

  public boolean add(T item) {
    int key = item.hashCode();
    while (true) {
      Node pred = this.head;
      Node curr = head.next;
      while (curr.key < key) {
        pred = curr; curr = curr.next;
      }
      pred.lock();
      try {
        curr.lock();
        try {
          if (validate(pred, curr)) {
            if (curr.key == key) { 
              return false;
            } else {               
              Node Node = new Node(item);
              Node.next = curr;
              pred.next = Node;
              return true;
            }
          }
        } finally { 
          curr.unlock();
        }
      } finally { 
        pred.unlock();
      }
    }
  }

  public boolean remove(T item) {
    int key = item.hashCode();
    while (true) {
      Node pred = this.head;
      Node curr = head.next;
      while (curr.key < key) {
        pred = curr; curr = curr.next;
      }
      pred.lock();
      try {
        curr.lock();
        try {
          if (validate(pred, curr)) {
            if (curr.key != key) {    
              return false;
            } else {                  
              curr.marked = true;     
              pred.next = curr.next;  
              return true;
            }
          }
        } finally {                   
          curr.unlock();
        }
      } finally {                    
        pred.unlock();
      }
    }
  }

  public boolean contains(T item) {
    int key = item.hashCode();
    Node curr = this.head;
    while (curr.key < key)
      curr = curr.next;
    return curr.key == key && !curr.marked;
  }

 public class Node {
    T item;
    int key; 
    Node next;
    boolean marked;
    Lock lock;
    Node(T item) {      
      this.item = item;
      this.key = item.hashCode();
      this.next = null;
      this.marked = false;
      this.lock = new ReentrantLock();
    }
   
    Node(int key) { 
      this.item = null;
      this.key = key;
      this.next = null;
      this.marked = false;
      this.lock = new ReentrantLock();
    }
   
    void lock() {lock.lock();}
    void unlock() {lock.unlock();}
  }
}


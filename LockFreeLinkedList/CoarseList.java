
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseList<T> implements List<T>{
 
  public Node head, tail;
 
  private Lock lock = new ReentrantLock();
  
  public CoarseList() {
    head  = new Node(Integer.MIN_VALUE);
    tail  = new Node(Integer.MAX_VALUE);
    head.next = this.tail;
  }
  

  public boolean add(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      if (key == curr.key) {
        return false;
      } else {
        Node node = new Node(item);
        node.next = curr;
        pred.next = node;
        return true;
      }
    } finally {
      lock.unlock();
    }
  }
  
  public boolean remove(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = this.head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      if (key == curr.key) {  
        pred.next = curr.next;
        return true;
      } else {
        return false;         
      }
    } finally {               
      lock.unlock();
    }
  }
  
  public boolean contains(T item) {
    Node pred, curr;
    int key = item.hashCode();
    lock.lock();
    try {
      pred = head;
      curr = pred.next;
      while (curr.key < key) {
        pred = curr;
        curr = curr.next;
      }
      return (key == curr.key);
    } finally {               
      lock.unlock();
    }
  }
  
  class Node {
    T item;
    int key;
    Node next;
    
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
    }
    
    Node(int key) {
      this.item = null;
      this.key = key;
    }
  }
}

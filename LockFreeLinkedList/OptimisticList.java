

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticList<T>  implements List<T>{
  
  Node head;
  public OptimisticList() {
    this.head  = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }
  
  public boolean add(T item) {
    int key = item.hashCode();
    while (true) {
      Node pred = this.head;
      Node curr = pred.next;
      while (curr.key <= key) {
        pred = curr; curr = curr.next;
      }
      pred.lock(); curr.lock();
      try {
        if (validate(pred, curr)) {
        	
          if (curr.key == key || pred.key == key) { 
            return false;
          } else {               
            Node entry = new Node(item);
            entry.next = curr;
            pred.next = entry;
            return true;
          }
        }
      } finally {                
        pred.unlock(); curr.unlock();
      }
    }
  }
  
  public boolean remove(T item) {
    int key = item.hashCode();
    while (true) {
      Node pred = this.head;
      Node curr = pred.next;
      while (curr.key < key) {
        pred = curr; curr = curr.next;
      }
      pred.lock(); curr.lock();
      try {
        if (validate(pred, curr)) {
          if (curr.key == key) { 
            pred.next = curr.next;
            return true;
          } else {               
            return false;
          }
        }
      } finally {                
        pred.unlock(); curr.unlock();
      }
    }
  }
  
  public boolean contains(T item) {
    int key = item.hashCode();
    while (true) {
      Node pred = this.head; 
      Node curr = pred.next;
      while (curr.key < key) {
        pred = curr; curr = curr.next;
      }
      try {
        pred.lock(); curr.lock();
        if (validate(pred, curr)) {
          return (curr.key == key);
        }
      } finally {                
        pred.unlock(); curr.unlock();
      }
    }
  }
 
  private boolean validate(Node pred, Node curr) {
    Node entry = head;
    while (entry.key <= pred.key) {
      if (entry == pred)
        return pred.next == curr;
      entry = entry.next;
    }
    return false;
  }
 
 class Node {
    T item;
    int key;
    Node next;
    Lock lock;
    Node(T item) {
      this.item = item;
      this.key = item.hashCode();
      lock = new ReentrantLock();
    }
  
    Node(int key) {
      this.key = key;
      lock = new ReentrantLock();
    }
    
    void lock() {lock.lock();}
    void unlock() {lock.unlock();}
  }
}

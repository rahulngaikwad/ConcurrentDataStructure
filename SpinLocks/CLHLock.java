import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.lang.ThreadLocal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock {

	AtomicReference<QNode> tail;
	ThreadLocal<QNode> myNode, myPred;

	public CLHLock() {
		tail = new AtomicReference<QNode>(new QNode());
		myNode = new ThreadLocal<QNode>() {
			protected QNode initialValue() {
				return new QNode();
			}
		};
		myPred = new ThreadLocal<QNode>() {
			protected QNode initialValue() {
				return null;
			}
		};
	}

	@Override
	public void lock() {
		QNode qnode = myNode.get(); 
		qnode.locked = true;
		QNode pred = tail.getAndSet(qnode);
		myPred.set(pred); 
		while (pred.locked) {
		} 
	}

	@Override
	public void unlock() {
		QNode qnode = myNode.get(); 
		qnode.locked = false; 
		myNode.set(myPred.get()); 
	}

	static class QNode { 
		public  volatile boolean locked = false;
	}

	
	@Override
	public Condition newCondition() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean tryLock() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new java.lang.UnsupportedOperationException();
	}
}

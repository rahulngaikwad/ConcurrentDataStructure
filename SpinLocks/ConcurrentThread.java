
import java.util.concurrent.locks.Lock;

public class ConcurrentThread extends Thread{
	 
	private final int noOfAttempts;
	private final Lock lock;
	public static int csCounter;

	public ConcurrentThread(Lock lock,int noOfAttempts){
		 this.lock = lock;
		 this.noOfAttempts = noOfAttempts;
	 }
	 
	 public void run() {		 
		for(int i = 0; i < noOfAttempts; i++){
			lock.lock();	
			try{
				criticalSection();
			}finally{
				lock.unlock();
			}
		}
     }

	 /**
	  * Dummy Critical Section
	  */
	private void criticalSection() {	
			csCounter++;
	}
 }
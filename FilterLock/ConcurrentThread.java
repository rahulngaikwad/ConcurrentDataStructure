public class ConcurrentThread extends Thread{
	 
	private final int noOfAttempts;
	private final int csTime;
	private Lock lock;

	public ConcurrentThread(Lock lock,int noOfAttempts, int csTime){
		 this.lock = lock;
		 this.noOfAttempts = noOfAttempts;
		 this.csTime = csTime;
	 }
	 
	 public void run() {		 
		for(int i = 0; i < noOfAttempts; i++){
			lock.lock();	
			try{
					//criticalSection();
			}finally{
			lock.unlock();
			}
		}
     }

	 /**
	  * Dummy Critical Section
	  */
	private void criticalSection() {
		System.out.println(ThreadID.get() + " Entering into CS");
		System.out.flush();
		try {
			Thread.sleep(csTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(ThreadID.get() + " Exiting  from CS");
		System.out.flush();
	}
 }
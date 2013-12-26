
public class MainDriver {
	private static final int NO_OF_THREADS = 30;
	private static final int NO_OF_CS_ATTEMPTS = 100;
	private static final int CS_TIME = 1;

	public static void main(String[] args) throws InterruptedException {	
		
		Lock lock = null;
		
		System.out.println("Lock type : MyLock");
		lock = new MyLock();
		for(int i = 1; i <= 10; i++){
			runExperimentAndShowResults(lock, NO_OF_THREADS, i*i*NO_OF_CS_ATTEMPTS);	
		}
			
		System.out.println("Lock type : FilterLock ");
		lock = new FilterLock(NO_OF_THREADS);
		for(int i = 1; i <= 10; i++){
			runExperimentAndShowResults(lock, NO_OF_THREADS, i*i*NO_OF_CS_ATTEMPTS);	
		}
	}


	private static void runExperimentAndShowResults(Lock lock, int noOfThreads, int noOfCsAttempts)
			throws InterruptedException {
		Thread[] ThreadSet = new Thread[noOfThreads];
		
		for(int i = 0; i < noOfThreads; i++){
			ThreadSet[i] = new ConcurrentThread(lock,noOfCsAttempts,0);		
		}
		ThreadID.set(0);
		Long startTime = System.currentTimeMillis();		
		for(int i = 0; i < noOfThreads; i++){
			ThreadSet[i].start();		
		}
		
		for(int i = 0; i < noOfThreads; i++){
			ThreadSet[i].join();
		}
		Long endTime = System.currentTimeMillis();
		
		System.out.println("No Of concurrent threads : " + noOfThreads);
		System.out.println("No Of CS attempts : " + noOfCsAttempts);
		System.out.println("Total time taken :" + (endTime- startTime));
		System.out.flush();
	}
}

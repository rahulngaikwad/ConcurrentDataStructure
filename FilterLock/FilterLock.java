
public class FilterLock implements Lock{
	  static final int INITIAL_LEVEL = 0;
	  volatile int[] level;
	  volatile int[] victim;
	  volatile int noOfThreads;
	  
	  public FilterLock(int noOfThreads) {
	    this.noOfThreads   = noOfThreads;
	    level  = new int[this.noOfThreads];
	    victim = new int[this.noOfThreads];
	  }
	  	  
	  public void lock() {
	    int me = ThreadID.get();
	    for (int l = 1; l < noOfThreads; l++) {
	      level[me]  = l;
	      victim[l] = me;
	      while (isAnyThreadAtSameOrHigherLevel(me, l) && victim[l] == me) {};
	    }   
	    //Thread reached here has acquired lock.
	  }
	  
	  private boolean isAnyThreadAtSameOrHigherLevel(int myId, int myLevel) {
	    for (int threadId = 0; threadId < noOfThreads; threadId++){
	      if (threadId != myId && level[threadId] >= myLevel) {
	        return true;
	      }
	    }
	    return false;
	  }
	    
	  public void unlock() {
	    int me = ThreadID.get();
	    level[me] = INITIAL_LEVEL;
	  }
}

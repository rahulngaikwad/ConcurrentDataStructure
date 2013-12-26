import java.util.Random;


public class ConcurrentThread extends Thread{
	 
	private final int noOfAttempts;
	private final List<Integer> list;
	public static int csCounter;
	private final int valueBound;

	public ConcurrentThread(List<Integer> list,int noOfAttempts, int valueBound){
		 this.list = list;
		 this.noOfAttempts = noOfAttempts;
		 this.valueBound = valueBound;
	 }
	 
	 public void run() {	
		 Random random = new Random();
		for(int i = 0; i < noOfAttempts; i++){
			int operation = random.nextInt(3);
			
			switch(operation)
			{
				case 0 : list.add(random.nextInt(valueBound));
						 break;
						 
				case 1 : list.remove(random.nextInt(valueBound));
				         break;
				         
				case 2 : list.contains(random.nextInt(valueBound));
			}
			
		}
     }
 }
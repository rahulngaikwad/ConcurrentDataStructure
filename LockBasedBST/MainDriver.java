


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class MainDriver {
	private static int MIN_NO_OF_THREADS = 10;
	private static int MAX_NO_OF_THREADS = 50;
	private static int THREAD_INCREAMENT = 2;
	private static int NO_OF_OPERATIONS = 100000;
	private static int VALUE_BOUND = 500;
	private static ArrayList<ArrayList<Long>> result =  new ArrayList<ArrayList<Long>>(5);
	public static void main(String[] args) {

		try {
			if (args.length >= 1) {
				readInputFile(args[0]);
			} else {
				readInputFile("input3.txt");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Please provide values for following variables");
			Scanner scn = new Scanner(System.in);
			
			System.out.println("MIN_NO_OF_THREADS = ");
			MIN_NO_OF_THREADS = scn.nextInt();
			
			System.out.println("MAX_NO_OF_THREADS = ");
			MAX_NO_OF_THREADS = scn.nextInt();
			
			System.out.println("THREAD_INCREAMENT = ");
			THREAD_INCREAMENT = scn.nextInt();
			
			System.out.println("NO_OF_OPERATIONS = ");
			NO_OF_OPERATIONS = scn.nextInt();
			
			System.out.println("VALUE_BOUND = ");
			VALUE_BOUND = scn.nextInt();
			scn.close();
		}

		
	
		System.out.println("No Of Operations : " + NO_OF_OPERATIONS);
		System.out.println("No Of Threads : " + MIN_NO_OF_THREADS);
		System.out.println("Value Bound : " + VALUE_BOUND);

		result.add(new ArrayList<Long>());
		for(int i = 0; MIN_NO_OF_THREADS + i*THREAD_INCREAMENT <= MAX_NO_OF_THREADS  ; i++){
			result.get(0).add( (long) (MIN_NO_OF_THREADS + i*THREAD_INCREAMENT));
		}
		
		for(int i = 1; i <= 4; i++){
			result.add(new ArrayList<Long>());
			runExperiment(i);
		}
		
		System.out.println("Thread, Coarse, Fine, Optimistic, Lazy");
		for(int i = 0; i < result.get(0).size(); i++){
			for(int j = 0; j < result.size(); j++)
				System.out.print(result.get(j).get(i)+"\t");
			System.out.println();
		}
	}
	


	private static void runExperiment( int index) {
		BinarySearchTree<Integer> bst = selectBST(index);
		System.out.println("no of Threads  , Time Taken " );
		try {
			for(int i = 0; MIN_NO_OF_THREADS + i*THREAD_INCREAMENT <= MAX_NO_OF_THREADS  ; i++){
				long timeTaken = showResults(bst, MIN_NO_OF_THREADS + i*THREAD_INCREAMENT , NO_OF_OPERATIONS);
				result.get(index).add(timeTaken);
			}
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static long showResults(BinarySearchTree<Integer> bst, int noOfThreads,
			int noOfCsAttempts) throws InterruptedException {

		Thread[] ThreadSet = new Thread[noOfThreads];

		ThreadID.reset();
		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i] = new ConcurrentThread(bst, noOfCsAttempts, VALUE_BOUND);
		}

		Long startTime = System.currentTimeMillis();

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].start();
		}

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].join();
		}

		Long endTime = System.currentTimeMillis();

		System.out.println(String.format("%-10d \t %-10d", noOfThreads, (endTime - startTime)));System.out.flush();
		return endTime - startTime;
	}
	
	static BinarySearchTree<Integer> selectBST(int index){
		switch(index){
		case 1: System.out.println("Coarse Grained BST");
				return new CoarseBST<Integer>();
				
		case 2: System.out.println("Fine Grained BST");
		return new FineBST<Integer>();
		
		case 3: System.out.println("Optimistic BST");
		return new OptimisticBST<Integer>();
		
		case 4: System.out.println("Lazy BST");
		return new LazyBST<Integer>();
				
		default:System.out.println("Unknown implementation"); 
				return null;	
		}
	}
	
	private static void readInputFile(String fileName)
			throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.contains("MIN_NO_OF_THREADS="))
					MIN_NO_OF_THREADS = Integer.parseInt(line.replace(
							"MIN_NO_OF_THREADS=", ""));
				else if (line.contains("MAX_NO_OF_THREADS="))
					MAX_NO_OF_THREADS = Integer.parseInt(line.replace(
							"MAX_NO_OF_THREADS=", ""));
				else if (line.contains("THREAD_INCREAMENT="))
					THREAD_INCREAMENT = Integer.parseInt(line.replace(
							"THREAD_INCREAMENT=", ""));
				else if (line.contains("NO_OF_OPERATIONS="))
					NO_OF_OPERATIONS = Integer.parseInt(line.replace(
							"NO_OF_OPERATIONS=", ""));
				else if (line.contains("VALUE_BOUND="))
					VALUE_BOUND = Integer
							.parseInt(line.replace("VALUE_BOUND=", ""));
			}// while ends

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

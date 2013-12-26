

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class MainDriver {
	private static int NO_OF_THREADS = 40;
	private static int NO_OF_ATTEMPTS = 1000;
	private static int VALUE_BOUND = 500;
	
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
			System.out.println("NO_OF_THREADS = ");
			NO_OF_THREADS = scn.nextInt();
			System.out.println("NO_OF_ATTEMPTS = ");
			NO_OF_ATTEMPTS = scn.nextInt();
			System.out.println("VALUE_BOUND = ");
			VALUE_BOUND = scn.nextInt();
			scn.close();
		}

		
	
		System.out.println("No Of cs attempts : " + NO_OF_ATTEMPTS);
		System.out.println("No Of Threads : " + NO_OF_THREADS);

		System.out.println("Coarse Grained Synchronization");
		runExperiment(new CoarseList<Integer>());

		System.out.println("Fine Grained Synchronization");
		runExperiment(new FineList<Integer>());
	
		System.out.println("Lazy Synchronization");
		runExperiment(new LazyList<Integer>());
		
		System.out.println("Optimistic Synchronization");
		runExperiment(new OptimisticList<Integer>());
		
		System.out.println("Non-Blocking Synchronization");
		runExperiment(new LockFreeList<Integer>());
	}

	private static void runExperiment(List<Integer> list) {
		System.out.println("no of Threads  , Time Taken " );
		try {
			for(int i = 1; i <=NO_OF_THREADS; i++)
			showResults(list, i, NO_OF_ATTEMPTS);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void showResults(List<Integer> list, int noOfThreads,
			int noOfCsAttempts) throws InterruptedException {

		Thread[] ThreadSet = new Thread[noOfThreads];

		ThreadID.reset();
		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i] = new ConcurrentThread(list, noOfCsAttempts, VALUE_BOUND);
		}

		Long startTime = System.currentTimeMillis();

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].start();
		}

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].join();
		}

		Long endTime = System.currentTimeMillis();

		System.out.println(String.format("%-10d \t %-10d", noOfThreads, (endTime - startTime)));
		//System.out.println(noOfThreads + "," + (endTime - startTime));;
		System.out.flush();
	}
	
	private static void readInputFile(String fileName)
			throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.contains("NO_OF_THREADS="))
					NO_OF_THREADS = Integer.parseInt(line.replace(
							"NO_OF_THREADS=", ""));
				else if (line.contains("NO_OF_ATTEMPTS="))
					NO_OF_ATTEMPTS = Integer.parseInt(line.replace(
							"NO_OF_ATTEMPTS=", ""));
				else if (line.contains("VALUE_BOUND="))
					VALUE_BOUND = Integer
							.parseInt(line.replace("VALUE_BOUND=", ""));
			}// while ends

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

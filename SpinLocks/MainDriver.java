
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class MainDriver {
	private static int NO_OF_THREADS = 4;
	private static int NO_OF_CS_ATTEMPTS = 100;
	private static int MIN_DELAY = 8;
	private static int MAX_DELAY = 64;

	public static void main(String[] args) {

		try {
			if (args.length >= 1) {
				readInputFile(args[0]);
			} else {
				readInputFile("input.txt");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Please provide values for following variables");
			Scanner scn = new Scanner(System.in);
			System.out.println("NO_OF_THREADS = ");
			NO_OF_THREADS = scn.nextInt();
			System.out.println("NO_OF_CS_ATTEMPTS = ");
			NO_OF_CS_ATTEMPTS = scn.nextInt();
			System.out.println("MIN_DELAY = ");
			MIN_DELAY = scn.nextInt();
			System.out.println("MAX_DELAY = ");
			MAX_DELAY = scn.nextInt();
			scn.close();
		}

		System.out.println("No Of cs attempts : " + NO_OF_CS_ATTEMPTS);
		System.out.println("No Of Threads : " + NO_OF_THREADS);

		System.out.println("Lock type : TASLock");
		runExperiment(new TASLock());

		System.out.println("Lock type : TTASLock");
		runExperiment(new TTASLock());

		System.out.println("Lock type : BackoffLock");
		runExperiment(new BackoffLock(MIN_DELAY, MAX_DELAY));

		System.out.println("Lock type : CLHLock");
		runExperiment(new CLHLock());
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
				else if (line.contains("NO_OF_CS_ATTEMPTS="))
					NO_OF_CS_ATTEMPTS = Integer.parseInt(line.replace(
							"NO_OF_CS_ATTEMPTS=", ""));
				else if (line.contains("MIN_DELAY="))
					MIN_DELAY = Integer
							.parseInt(line.replace("MIN_DELAY=", ""));
				else if (line.contains("MAX_DELAY="))
					MAX_DELAY = Integer
							.parseInt(line.replace("MAX_DELAY=", ""));
			}// while ends

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void runExperiment(Lock lock) {
		System.out.print("Time Taken = " );
		try {
			showResults(lock, NO_OF_THREADS, NO_OF_CS_ATTEMPTS);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void showResults(Lock lock, int noOfThreads,
			int noOfCsAttempts) throws InterruptedException {

		Thread[] ThreadSet = new Thread[noOfThreads];

		ThreadID.reset();
		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i] = new ConcurrentThread(lock, noOfCsAttempts);
		}

		Long startTime = System.currentTimeMillis();

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].start();
		}

		for (int i = 0; i < noOfThreads; i++) {
			ThreadSet[i].join();
		}

		Long endTime = System.currentTimeMillis();

		System.out.println(String.format("%-10d",(endTime - startTime)));
		System.out.flush();
	}
}

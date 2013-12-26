   
**************** How to compile and run ******************************
1. Copy all .java files 
2. Compile all files using javac and junit.jar
   e.g javac -cp junit.jar *.java
3. Open input.txt and supply config parameters i.e. number of threads, number of CS attempts min and max delay for backoff
	e.g
	NO_OF_THREADS=4
	NO_OF_CS_ATTEMPTS=100
	MIN_DELAY=4
	MAX_DELAY=64
4. run program using MainDriver java class and check output
5. To check correctness of each clock please run test file of corresponding lock.
6. please find attached report for performance comparision.

e.g
{cslinux1:~/ConDS/prj1} vi MainDriver.java
{cslinux1:~/ConDS/prj1} javac *.java
{cslinux1:~/ConDS/prj1} java MainDriver

*********output*************************
No Of cs attempts : 100
No Of Threads : 4

 Lock type : TASLock
Time Taken 1         
Lock type : TTASLock
Time Taken 0         
Lock type : BackoffLock
Time Taken 6         
Lock type : CLHLock
Time Taken 1         

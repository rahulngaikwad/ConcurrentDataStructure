CONCURRENT DATA STRUCTURES FOR MULTICORE SYSTEMS

**************** How to compile and run ******************************
1. Copy all files from dir Source  
2. Compile all files using junit.jar 
   e.g javac -cp junit.jar  *.java
3. Open input3.txt and supply config parameters i.e. number of threads, number of CS attempts and valu bound
	e.g
	NO_OF_THREADS=10
	NO_OF_CS_ATTEMPTS=10000
	VALUE_BOUND=100
4. run program using MainDriver java class and check output
e.g java MainDriver
5. To check correctness of please run corrsponding test file.
e.g 
java -cp :junit.jar junit.textui.TestRunner CoarseListTest
java -cp :junit.jar junit.textui.TestRunner FineListTest
java -cp :junit.jar junit.textui.TestRunner LazyListTest
java -cp :junit.jar junit.textui.TestRunner LockFreeListTest
java -cp :junit.jar junit.textui.TestRunner OptimisticListTest

PERFORMANCE ANALYSIS:
---------------------
Please Refer to the file Report.docx

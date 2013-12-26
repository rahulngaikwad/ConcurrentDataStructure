
**************** How to compile and run ******************************
1. Copy all .java files 
2. Open MainDriver.java and supply number of threads and number of CS attempts
	e.g
	NO_OF_THREADS = 30;
	NO_OF_CS_ATTEMPTS = 100;
3. Compile all files using javac
4. run program using MainDriver  java class and check output
e.g
{cslinux1:~/ConDS/prj1} vi MainDriver.java
{cslinux1:~/ConDS/prj1} javac *.java
{cslinux1:~/ConDS/prj1} java MainDriver

********************Question/Answeres*****************************************
1. Filter algorithm
   Please find FilterLock.java
2. List all atomic instructions supported by one of the cs machines (cs1 or cs2).
   ADD, ADC, AND, BTC, BTR, BTS, CMPXCHG, CMPXCH8B, CMPXCHG16B, DEC, INC, NEG, NOT, OR, SBB, SUB, XOR, XADD, and XCHG

	ADC—Add with Carry 
	ADD—Add
	AND—Logical AND
	BTC—Bit Test and Complement
	BTR—Bit Test and Reset
	BTS—Bit Test and Set
	CMPXCHG —Compare and Exchange
	CMPXCHG8B/CMPXCHG16B —Compare and Exchange Bytes
	DEC—Decrement by 1
	INC—Increment by 1
	NEG—Two's Complement Negation
	NOT—One's Complement Negation
	OR—Logical Inclusive OR
	SBB—Integer Subtraction with Borrow
	SUB—Subtract
	XOR—Logical Exclusive OR
	XADD—Exchange and Add
	XCHG —Exchange Register/Memory with Register

3. Implement a solution for the mutual exclusion problem using any two of the atomic instruc-
tions in the above list. 
	Please find MyLock.java

4. Compare the performance of achieving mutual exclusion using Filter algorithm with the one
obtained using an atomic instruction (of your choice).
	
	It looks like MyLock performs mutch better than FilterLock as no of threads increases.
	
No of threads = 10
No Of CS Attempts   Total Time-MyLock   Time Taken-FilterLock
	100					4					12
	400					11					16
	900					10					24
	1600				10					29
	2500				10					68
	3600				14					92
	4900				18					129
	6400				23					181
	8100				29					213
	10000				38					259

No of threads = 20
No Of CS Attempts   Total Time-MyLock   Time Taken-FilterLock
	100					33					384
	400					31					560
	900					73					2226
	1600				162					4311
	2500				297					7166
	3600				447					10420
	4900				548					14287
	6400				878					19173
	8100				1043				21314
	10000				1208				28865

No of threads = 30
No Of CS Attempts   Total Time-MyLock   Time Taken-FilterLock
	100					53					2757
	400					50					10647
	900					126					21059
	1600				316					40083
	2500				560					65358
	3600				935					94954
	4900				1550				130181
	6400				1989				167658
	8100				2308				212524
	10000				2681				248244
	
No of threads = 40
No Of CS Attempts   Total Time-MyLock   Time Taken-FilterLock
	100					36				26280
	400					55				132627
	900					244				215788
	1600				383				436720
	2500				1337			-
	3600				1625			-
	4900				2264			-
	6400				2675			-
	8100				3949			-
	10000				4844			-

	
*******************usefull commands*******************************
 cat /proc/cpuinfo
 lscpu
 uname -a
 
 

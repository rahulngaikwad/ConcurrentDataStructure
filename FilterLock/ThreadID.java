import java.util.concurrent.atomic.AtomicInteger;


public class ThreadID {

	// Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
            @Override protected Integer initialValue() {
                return nextId.getAndIncrement();
        }
    };

    public static void set(int id) {
    	nextId.set(id);
    }
    
    // Returns the current thread's unique ID, assigning it if necessary
    public static int get() {
        return threadId.get();
    }


}


public class ConcurrentCounters {

    // Shared Resources
    private static volatile int shared_counter = 0; // Counter shared between threads.
    private static final Object counter_mutex = new Object(); // Protects shared_counter.
    private static boolean count_up_finished = false; // Flag indicating if "count up" thread is done.

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Program started.");

        // Create two threads for "count up" and "count down" processes
        Thread up_thread = new Thread(ConcurrentCounters::countUp, "CountUpThread");
        Thread down_thread = new Thread(ConcurrentCounters::countDown, "CountDownThread");

        System.out.println("Main: Starting CountUpThread...");
        up_thread.start();

        System.out.println("Main: Starting CountDownThread (will wait for CountUpThread to finish)...");
        down_thread.start();

        // Wait for both threads to complete
        up_thread.join();   // 'join()' pauses the main thread until 'up_thread' is done.
        down_thread.join(); // 'join()' pauses the main thread until 'down_thread' is done.

        // After both threads have finished, print update message with the final value of the shared_counter.
        System.out.println("Both threads finished. Final shared_counter: " + shared_counter);
        System.out.println("Program finished.");
    }

    // Function for the "count up" thread
    private static void countUp() {
        System.out.println(Thread.currentThread().getName() + ": Started.");
        // Iterate from 0 to 20
        for (int i = 0; i <= 20; ++i) {
            // Acquire lock on the counter_mutex using a synchronized block.
            synchronized (counter_mutex) {
                // Set shared_counter to current iteration
                shared_counter = i;
                // Print current count up value progress
                System.out.println(Thread.currentThread().getName() + ": " + shared_counter);
            } // The synchronized block ends here, automatically releasing the lock.

            // Simulate work and thread communication using a pause.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + ": Interrupted.");
                return;
            }
        }

        // Acquire lock again to update shared flag and notify waiting "count down" thread.
        synchronized (counter_mutex) {
            // Set flag to true indicating count up is complete
            count_up_finished = true;
            // Print thread counting up update message
            System.out.println(Thread.currentThread().getName() + " finished. Notifying Count Down thread.");
            // Notify waiting "count down" thread that the condition (count_up_finished) has been met.
            counter_mutex.notifyAll(); // Using notifyAll() as good practice, though notify() would suffice here.
        } // Lock is once again out of scope and automatically released.
    }

    // Function for the "count down" thread
    private static void countDown() {
        System.out.println(Thread.currentThread().getName() + ": Started.");
        // Acquire lock on the counter_mutex.
        synchronized (counter_mutex) {
            System.out.println(Thread.currentThread().getName() + ": Waiting for Count Up to finish...");
            // Wait on 'counter_mutex'. This pauses the current thread and releases the lock until 'notifyAll' is called
            // and 'count_up_finished' is true.
            while (!count_up_finished) {
                try {
                    counter_mutex.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + ": Interrupted while waiting.");
                    return;
                }
            }
            // After 'count_up_finished' is true, the thread resumes, and the lock is reacquired.

            // Print starting count down value (which is the final value from the "count up" thread.)
            System.out.println(Thread.currentThread().getName() + ": Starting from " + shared_counter);
        } // The synchronized block ends here, automatically releasing the lock.

        // Get the current value of shared_counter outside the synchronized block to start the loop.
        int startValue = shared_counter;
        // Iterate from shared_counter down to 0
        for (int i = startValue; i >= 0; --i) {
            // Acquire lock for each update to shared_counter within the loop, enabling atomic operations.
            synchronized (counter_mutex) {
                // Set shared_counter to current iteration
                shared_counter = i;
                // Print current count down value progress
                System.out.println(Thread.currentThread().getName() + ": " + shared_counter);
            } // The lock is automatically released here.

            // Simulate work using a pause.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + ": Interrupted.");
                return;
            }
        }
        System.out.println(Thread.currentThread().getName() + ": Finished.");
    }
}
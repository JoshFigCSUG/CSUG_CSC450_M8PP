## Concurrent Counters Application

This README provides specific details about the `ConcurrentCounters.java` source code, which is the core of this Java application demonstrating concurrency concepts.

### Overview

`ConcurrentCounters.java` implements a multithreaded program that utilizes two threads to manage a shared counter. One thread increments the counter to a specific value, and upon completion, the second thread decrements the counter back to zero. This design showcases thread synchronization and communication mechanisms in Java.

### Key Components

The `ConcurrentCounters.java` file defines the following:

#### Shared Resources
* `shared_counter`: A `volatile` integer that serves as the main counter, accessible and modified by both threads. The `volatile` keyword ensures that changes to `shared_counter` are immediately visible to all threads.
* `counter_mutex`: An `Object` used as a lock to protect the `shared_counter` during critical sections, preventing race conditions.
* `count_up_finished`: A `boolean` flag that signals when the "count up" thread has completed its operation, allowing the "count down" thread to proceed.

#### Main Method (`main`)
The `main` method orchestrates the execution of the two counter threads.
1.  It creates two `Thread` objects: `up_thread` and `down_thread`.
2.  `up_thread` is started, followed by `down_thread`.
3.  The `main` thread waits for both `up_thread` and `down_thread` to complete using the `join()` method, ensuring proper termination and preventing the main thread from exiting prematurely.
4.  Finally, it prints the final value of `shared_counter`.

#### Count Up Function (`countUp`)
This function is executed by `up_thread`.
* It increments `shared_counter` from 0 to 20.
* Each update to `shared_counter` is enclosed within a `synchronized (counter_mutex)` block to ensure thread-safe access and prevent data corruption.
* A `Thread.sleep(100)` call simulates work and allows for interleaved execution with other threads.
* After reaching 20, it sets `count_up_finished` to `true` and calls `counter_mutex.notifyAll()` to wake up any threads waiting on `counter_mutex` (specifically, `down_thread`).

#### Count Down Function (`countDown`)
This function is executed by `down_thread`.
* It initially enters a `synchronized (counter_mutex)` block and uses a `while (!count_up_finished)` loop with `counter_mutex.wait()` to pause its execution until `up_thread` signals completion.
* Once `up_thread` finishes, `down_thread` resumes and starts decrementing `shared_counter` from its current value (which is 20) down to 0.
* Similar to `countUp`, each decrement is protected by a `synchronized (counter_mutex)` block.
* It also includes a `Thread.sleep(100)` to simulate work.

This application effectively demonstrates basic principles of multithreading, shared resource management, and inter-thread communication using Java's `synchronized`, `wait()`, and `notifyAll()` mechanisms.

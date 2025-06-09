# CSUG_CSC450_M8PP
CSUG_CSC450_M8PP
## Project Overview

This project, `CSUG_CSC450_M8PP`, is a Java application designed to demonstrate concurrency concepts. It features two threads that act as counters: one thread counts up to 20, and once it reaches 20, a second thread counts down to 0.

### Getting Started

Welcome to the VS Code Java world. This project is structured to help you get started with Java development in Visual Studio Code.

### Folder Structure

By default, the workspace contains two main folders:
* `src`: This folder is used to maintain source code.
* `lib`: This folder is used to maintain project dependencies.

Compiled output files are generated in the `bin` folder by default. You can customize this folder structure by updating the `.vscode/settings.json` file.

### Dependency Management

The `JAVA PROJECTS` view in VS Code allows you to manage your project's dependencies.

### Application Details: `ConcurrentCounters.java`

The `ConcurrentCounters.java` file implements the core logic for the concurrent counters.

* **Shared Resources**
    * `shared_counter`: An integer counter shared between threads, declared as `volatile`.
    * `counter_mutex`: An object used as a mutex to protect `shared_counter`.
    * `count_up_finished`: A boolean flag indicating when the "count up" thread has completed.
* **Main Function (`main`)**
    * Initializes the program and creates two threads: `up_thread` (for counting up) and `down_thread` (for counting down).
    * Starts `up_thread` first, then `down_thread` (which waits for `up_thread` to finish).
    * Uses `join()` to ensure the main thread waits for both `up_thread` and `down_thread` to complete before printing the final `shared_counter` value.
* **Count Up Function (`countUp`)**
    * Iterates from 0 to 20.
    * Acquires a lock on `counter_mutex` using a `synchronized` block before updating `shared_counter` to ensure atomic operations.
    * Simulates work with a 100-millisecond pause (`Thread.sleep(100)`) in each iteration.
    * After completing the count up, it re-acquires the lock, sets `count_up_finished` to `true`, and calls `notifyAll()` on `counter_mutex` to signal the waiting "count down" thread.
* **Count Down Function (`countDown`)**
    * Acquires a lock on `counter_mutex` and waits for `count_up_finished` to be `true` using a `while` loop and `counter_mutex.wait()`.
    * Once `count_up_finished` is true, it resumes, reacquires the lock, and prints the starting value for the countdown (which is the final value from the count up thread).
    * Iterates from the `startValue` (the final value of `shared_counter` from the count-up thread) down to 0.
    * Acquires a lock on `counter_mutex` for each update to `shared_counter` within the loop.
    * Simulates work with a 100-millisecond pause (`Thread.sleep(100)`) in each iteration.

## Project Prompt

For your portfolio project, you will demonstrate an understanding of the various concepts discussed in each module. For the second part of your portfolio project, you will create a Java application that will exhibit concurrency concepts. Your application should create two threads that will act as counters. One thread should count to 20. Once thread-one reaches 20, then a second thread should be used to countdown to 0. For your created code, please provide a detailed analysis of appropriate concepts that could impact your application. Specifically, address: Performance issues with concurrency, vulnerabilities exhibited with use of strings, and security of the data types exhibited.

package com.cresoftlab.part1;

public class Example1 {

    /**
     * Entry point of the application.
     * <p>
     * Calls two demonstration methods:
     * <ul>
     *   <li>{@link #method1()} — prints information about the current thread</li>
     *   <li>{@link #method2()} — prints information about all currently active threads</li>
     * </ul>
     * Each method call is preceded by a label printed to the console.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("---Call method1---");
        method1();

        System.out.println("---Call method2---");
        method2();

        System.out.println("---Call method3---");
        method3();

        System.out.println("---Call method4---");
        method4();
    }

    /**
     * Prints information about the currently executing thread.
     * <p>
     * Uses {@link Thread#currentThread()} to obtain the thread in which the
     * current code is running and prints it to the console.
     * </p>
     * <p>
     * The output is produced by {@link Thread#toString()} and has the format:
     * </p>
     * <pre>
     * Thread[name, priority, threadGroup]
     * </pre>
     * Example:
     * <pre>
     * Thread[main,5,main]
     * </pre>
     */
    public static void method1() {
        Thread thread = Thread.currentThread();
        System.out.println(thread);
    }

    /**
     * Prints information about all currently active threads.
     * <p>
     * Uses {@link Thread#getAllStackTraces()} to obtain a map where the keys
     * represent all live threads and the values contain their stack traces.
     * In this example only the thread objects (keys) are used.
     * </p>
     * <p>
     * Each thread is printed using {@link Thread#toString()}, which shows
     * the thread name, priority, and thread group.
     * </p>
     */
    public static void method2() {
        // obtain all currently running threads (keys of the map)
        var threads = Thread.getAllStackTraces().keySet();

        for (var thread : threads) {
            System.out.println(thread);
        }
    }

    public  static void method3() {
        // get current thread
        Thread t = Thread.currentThread();
        // get thread properties

        System.out.println("Id: " + t.threadId());
        System.out.println("Name: " + t.getName());
        System.out.println("Priority: " + t.getPriority());
        System.out.println("State: " + t.getState());
        System.out.println("isAlive: " + t.isAlive());
        System.out.println("isDaemon: " + t.isDaemon());
        System.out.println("isVirtual: " + t.isVirtual());
    }

    /**
     * Demonstrates creating and starting a new thread using a {@link Runnable}.
     * <p>
     * The method prints a message from the main thread, then creates a
     * {@link Runnable} task that prints start and finish messages.
     * The task is executed in a separate thread created via {@link Thread}.
     * </p>
     * <p>
     * Because the new thread runs concurrently with the main thread,
     * the order of printed messages may vary depending on the thread scheduler.
     * </p>
     */
    public static void method4() {
        System.out.println("Main thread started...");

        Runnable task1 = () -> {
            System.out.println("Runnable task1 started...");
            System.out.println("Runnable task1 finished...");
        };

        var thread1 = new Thread(task1);
        thread1.start();

        System.out.println("Main thread started...");
    }

}
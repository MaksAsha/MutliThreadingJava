package com.cresoftlab.part2;

public class Main {

    public static void main(String[] args) {
        method1();
        method2();
        method3();
    }

    public static void method1() {
        System.out.println("Main thread1 started...");

        Runnable task = new MyRunnableTask();

        var t = new Thread(task);  // define thread

        t.start();  // run thread

        System.out.println("Main thread1 finished...");
    }

    public static void method2() {
        System.out.println("Main thread2 started...");

        Thread thread = new MyThreadTask();
        thread.start();

        System.out.println("Main thread2 finished...");
    }

    public static void method3() {
        System.out.println("Main thread2 started...");

        for(int i=1; i < 6; i++){
            var task = new MyRunnableTaskWithName("task_" + i);
            //task.run();
            new Thread(task).start();
        }

        System.out.println("Main thread2 finished...");
    }

}

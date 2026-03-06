package com.cresoftlab.part2;

/**
 * Class extended from Thread.
 */
public class MyThreadTask extends Thread {

    public MyThreadTask() {
        super();
    }

    @Override
    public void run() {
        System.out.println("MyThreadTask run started...");
        System.out.println("MyThreadTask run finished...");
    }
}

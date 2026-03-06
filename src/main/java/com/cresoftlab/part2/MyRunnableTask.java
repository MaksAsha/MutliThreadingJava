package com.cresoftlab.part2;

/**
 * Class implemented Runnable interface.
 */
public class MyRunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable started...");
        System.out.println("Runnable finished...");

    }
}

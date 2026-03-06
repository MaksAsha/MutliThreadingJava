package com.cresoftlab.part2;

/**
 * Class implemented runnable interface with name.
 */
public class MyRunnableTaskWithName implements Runnable {
    private String name;

    public MyRunnableTaskWithName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("MyRunnableTaskWithName " + name + " run started...");
        System.out.println("MyRunnableTaskWithName " + name + " run finished...");
    }
}

package com.cresoftlab.part3;

class MyTask implements Runnable {

    public void run(){

        String name = Thread.currentThread().getName();  // obtain the name of current thread

        System.out.println(name + " started...");

        System.out.println(name + " works");

        try{
            Thread.sleep(1000); // stop thread for 1000 ms
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(name + " finished...");
    }
}
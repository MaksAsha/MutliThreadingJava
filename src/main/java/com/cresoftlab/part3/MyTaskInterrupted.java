package com.cresoftlab.part3;

class MyTaskInterrupted implements Runnable {

    public void run(){

        String name = Thread.currentThread().getName();  // obtain the name of current thread

        try{

            System.out.println(name + " started...");

            while(!Thread.currentThread().isInterrupted()){

                System.out.println(name + " works");

                Thread.sleep(900); // stop thread for 900 ms
            }
        }
        catch(InterruptedException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println(name + " finished...");
    }
}
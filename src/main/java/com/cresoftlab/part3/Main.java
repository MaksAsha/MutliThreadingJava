package com.cresoftlab.part3;

public class Main {
    public static void main(String[] args) {
        //method1();
        method2();
    }

    public static void method1(){
        System.out.println("Main thread started...");

        var task = new MyTask();
        var t = new Thread(task, "MyTask");
        t.start();

        try{
            t.join();  // current Main thread is waiting of finishing thread t
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }

        System.out.println("Main thread finished...");
    }

    public static void method2(){
        System.out.println("Main thread started...");

        // define thread
        var t = new Thread(new MyTaskInterrupted(), "MyTask");
        // запускаем поток
        t.start();

        try{
            //introduce short delay so that the child thread MyTask has time to do some work
            Thread.sleep(2000);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }

        // interrupt the thread
        t.interrupt();

        System.out.println("Main thread finished...");
    }
}

package com.cresoftlab.part4;

public class CommonResource2 {
    int x = 0;

    synchronized void increment(){  // синхронизированный метод

        x = 1;
        for (int i = 1; i < 5; i++){
            System.out.printf("%s %d \n", Thread.currentThread().getName(), x);
            x++;
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException _){}
        }
    }
}

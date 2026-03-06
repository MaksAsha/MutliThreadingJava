package com.cresoftlab.part10;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        CommonResource commonResource= new CommonResource();
        ReentrantLock locker = new ReentrantLock(); // создаем заглушку
        for (int i = 1; i < 6; i++){
            new Thread(new CountThread(commonResource, locker), "Thread "+ i).start();
        }
    }
}

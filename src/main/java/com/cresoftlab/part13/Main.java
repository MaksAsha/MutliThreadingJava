package com.cresoftlab.part13;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        int incrementsPerThread = 1000;

        Counter counter = new Counter();

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++){

            threads[i] = new Thread(() ->{

                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        // небольшая задержка, чтобы потоки успели поработать
        Thread.sleep(1000);

        // ожидаем завершения всех потоков
        for (int i = 0; i < numThreads; i++){
            threads[i].join();
        }

        // проверяем значение счетчика
        System.out.println("Counter: " + counter.getCounter());
    }
}

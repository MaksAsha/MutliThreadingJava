package com.cresoftlab.part12;

class Worker implements Runnable {

    // Флаг НЕ volatile
    private volatile boolean running = true;

    @Override
    public void run() {

        // Этот код выполняется в "Потоке А"
        System.out.println("Поток А: Начинаю работу...");
        while (running) {
            // Здесь может быть некоторая работа

            // JIT-компилятор с высокой вероятностью "закэширует" переменную running
            // в регистре процессора, так как она не меняется внутри цикла.
        }
        // Эта строка может никогда не выполниться
        System.out.println("Поток А: Работа остановлена.");
    }

    public void stop() {
        // Этот код выполняется в "Потоке Б" (главном потоке)
        System.out.println("Поток Б: Отправляю команду на остановку...");
        this.running = false;
    }
}
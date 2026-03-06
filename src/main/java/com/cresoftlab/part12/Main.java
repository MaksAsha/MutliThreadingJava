package com.cresoftlab.part12;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();

        // Создаем "Поток А" и запускаем его. Он вызовет worker.run()
        Thread workerThread = new Thread(worker);
        workerThread.start(); // <-- Здесь запускается run()

        // Даем "Потоку А" немного времени, чтобы он вошел в цикл while(true)
        Thread.sleep(1000); // Ждем 1 секунду

        // "Поток Б" (главный поток) вызывает stop()
        worker.stop(); // <-- Здесь вызывается stop()

        // Ждем завершения "Потока А"
        // Если флаг running не был volatile, join() может ждать вечно!
        workerThread.join(2000); // Ждем до 2 секунд

        if (workerThread.isAlive()) {
            System.out.println("--- РЕЗУЛЬТАТ: Поток А все еще работает! ---");
            System.out.println("Поток А не увидел изменение running = false.");
            System.exit(1); // Завершаем программу, так как поток "завис"
        } else {
            System.out.println("--- РЕЗУЛЬТАТ: Поток А  успешно завершился. ---");
        }
    }
}

package com.cresoftlab.part7;

import java.util.concurrent.Semaphore;

class Philosopher extends Thread
{
    Semaphore sem; // semaphore limiting the number of philosophers
    // number of meals
    int num = 0;
    // indexed philosopher number
    int id;
    // в качестве параметров конструктора передаем идентификатор философа и семафор
    Philosopher(Semaphore sem, int id)
    {
        this.sem=sem;
        this.id=id;
    }

    public void run()
    {
        try
        {
            while(num<3)// пока количество приемов пищи не достигнет 3
            {
                //Запрашиваем у семафора разрешение на выполнение
                sem.acquire();
                System.out.println ("Философ " + id+" садится за стол");
                // философ ест
                sleep(500);
                num++;

                System.out.println ("Философ " + id+" выходит из-за стола");
                sem.release();

                // философ гуляет
                sleep(500);
            }
        }
        catch(InterruptedException e)
        {
            System.out.println ("у философа " + id + " проблемы со здоровьем");
        }
    }
}
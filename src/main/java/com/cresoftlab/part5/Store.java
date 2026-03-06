package com.cresoftlab.part5;

/**
 * Class Store, keeping produced goods.
 */
class Store {
    private int product = 0;

    public synchronized void get() {
        while (product < 1) {
            try {
                wait();
                System.out.println("Waiting1 for product " + product);
            } catch (InterruptedException e) {
            }
        }
        product--;
        System.out.println("Customer bought 1 product");
        System.out.println("Goods at the store: " + product);
        notify();
    }

    public synchronized void put() {
        while (product >= 3) {
            try {
                wait();
                System.out.println("Waiting2 for product " + product);
            } catch (InterruptedException e) {
            }
        }
        product++;
        System.out.println("Producer added 1 item");
        System.out.println("Goods at the store: " + product);
        notify();
    }
}
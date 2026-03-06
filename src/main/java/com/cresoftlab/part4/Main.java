package com.cresoftlab.part4;

public class Main {

    public static void main(String[] args) {
        //method1();
        method2();
    }

    public static void method1(){
        CommonResource commonResource= new CommonResource();

        for (int i = 1; i < 6; i++){

            new Thread(new CountThread(commonResource), "Thread "+ i).start();
        }

    }

    public static void method2(){
        CommonResource2 commonResource= new CommonResource2();

        for (int i = 1; i < 6; i++){

            new Thread(new CountThread2(commonResource), "Thread "+ i).start();
        }

    }

}


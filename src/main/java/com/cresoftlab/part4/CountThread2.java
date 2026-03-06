package com.cresoftlab.part4;

class CountThread2 implements Runnable{

    CommonResource2 res;

    CountThread2(CommonResource2 res){
        this.res = res;
    }

    public void run(){
        res.increment();
    }
}
package com.king.juc.future;


import java.util.concurrent.Callable;

/**
 * 仿造FutureTask的实现原理，实现替换
 *
 * 核心关键点
 * 1、构造函数传入callable
 * 2、有泛型定义
 * 3、实现了runable
 * 4、get方法，能返回运算结果，并且有阻塞功能
 */
public class MyFutureTask<T> implements Runnable{

    //封装了业务逻辑的callable对象
    private Callable<T> callable;
    //执行后的结果
    private T result;

    String state="new";
    public MyFutureTask(Callable<T> callable){
        this.callable=callable;
    }

    @Override
    public void run() {
        //执行业务逻辑
        try {
            result  = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            state="end";
        }

        synchronized (this){
            System.out.println(Thread.currentThread().getName() + "生成者结束通知");
            this.notifyAll();
        }
    }

    public T get(){
        if("end".equals(state)) return result;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this){
            try {
                System.out.println(Thread.currentThread().getName() + "消费者进入等待");
                //容易造成假死（线程先notifyAll，再wait就会进入一直等待状态）
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;


    }

}

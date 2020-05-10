package com.king.juc.future;


import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 为了避免假死，将 wait。notefiyAll替换成park,unpark
 */
public class MyFutureTask2<T> implements Runnable{

    //封装了业务逻辑的callable对象
    private Callable<T> callable;
    //执行后的结果
    private T result;
    String state="new";

    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MyFutureTask2(Callable<T> callable){
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

        while (true){
            Thread waiter = waiters.poll();
            if(waiter==null){
                break;
            }
            LockSupport.unpark(waiter);
        }
        System.out.println(Thread.currentThread().getName() + "生成者结束通知");
    }

    public T get(){
        if("end".equals(state)) return result;
        Thread currentThread = Thread.currentThread();
        waiters.add(currentThread);

        try {
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + "消费者进入等待");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.park(currentThread);
        return result;


    }

}

package com.king.juc.controller;

import com.alibaba.fastjson.JSONObject;
import com.king.juc.future.MyFutureTask;
import com.king.juc.future.MyFutureTask2;
import com.king.juc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


/**
 * 场景：
 * 一个接口通过并行快速获取信息
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUserInfo")
    public Object getUserInfo(){

        long start = System.currentTimeMillis();

        //有返回值的runable
        Callable<JSONObject> baseInfoCallable = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {

                long baseInfoTime = System.currentTimeMillis();
                //模拟调用接口或者查询数据库获取用户基本信息
                JSONObject baseInfo = userService.baseInfo();
                System.out.println("获取用户基本信息接口消耗"+(System.currentTimeMillis()-baseInfoTime));
                return baseInfo;
            }
        };
        Callable<JSONObject> pointInfoCallable = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                long pointInfoTime = System.currentTimeMillis();
                //模拟调用接口或者查询数据库获取用户积分信息
                JSONObject pointInfo = userService.pointInfo();
                System.out.println("获取用积分信息接口消耗"+(System.currentTimeMillis()-pointInfoTime));
                return pointInfo;
            }
        };
        //包装成FutureTask
        MyFutureTask<JSONObject> baseInfoFT = new MyFutureTask<>(baseInfoCallable);
        MyFutureTask<JSONObject> pointInfoFT = new MyFutureTask<>(pointInfoCallable);
        //开启线程
        new Thread(baseInfoFT).start();
        new Thread(pointInfoFT).start();

        JSONObject result = new JSONObject();
        try {
            //等待线程执行完毕，再拿结果
            //线程阻塞效果，如果task没有执行结果，get方法会阻塞main线程，代码不往下跑
            result.putAll(baseInfoFT.get());
            result.putAll(pointInfoFT.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        //消耗时间
        long consume = end - start;
        System.out.println("接口总共消耗"+consume);
        result.put("consume",consume);
        return result;
    }
}

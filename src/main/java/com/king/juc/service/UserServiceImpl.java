package com.king.juc.service;

import com.alibaba.fastjson.JSONObject;
import com.king.juc.pojo.UserBaseInfo;
import com.king.juc.pojo.UserPointInfo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public  JSONObject baseInfo() {
        //UserBaseInfo userBaseInfo = new UserBaseInfo();
        JSONObject userBaseInfo = new JSONObject();
        try {
            userBaseInfo.put("userId",1);
            userBaseInfo.put("name","king");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userBaseInfo;
    }

    @Override
    public  JSONObject pointInfo() {

        JSONObject userPointInfo = new JSONObject();
        //Map<String,Object> userPointInfo = new HashMap<>();
        try {
            userPointInfo.put("point",100);
            userPointInfo.put("userId",1);
            userPointInfo.put("update",new Date());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userPointInfo;
    }
}

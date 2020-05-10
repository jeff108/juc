package com.king.juc.service;

import com.alibaba.fastjson.JSONObject;
import com.king.juc.pojo.UserBaseInfo;
import com.king.juc.pojo.UserPointInfo;

import java.util.Map;

public interface UserService {

    JSONObject baseInfo();

    JSONObject pointInfo();
}

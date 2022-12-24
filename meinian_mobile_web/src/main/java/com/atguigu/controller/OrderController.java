package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result saveOrder(@RequestBody Map map) {
        //用户短信
        String validateCode = (String) map.get("validateCode");
        //redis缓存短信
        String telephoneCode = jedisPool.getResource().get(map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);
        if (validateCode.equals(telephoneCode) && telephoneCode != null) {
            Result result = null;
            try {
                result = orderService.saveOrder(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }
}

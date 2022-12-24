package com.atguigu.dao;

import com.atguigu.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    //根据会员idmember_id,日期orderDate,旅游idsetmeal_id 查询
    List<Order> getOrderByParam(Order order);

    //添加订单表
    void addOrder(Order order);
}

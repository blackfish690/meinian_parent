package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    //查询数据库是否有这个数据
    long findCountByOrderDate(Date orderDate);

    //根据日期修改可预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);

    //添加新的日期和可预约人数
    void add(OrderSetting orderSetting);

    //查询当月可预约的日期
    List<OrderSetting> getOrderSettingByMonth(@Param("startDate") String startDate, @Param("endData") String endData);

    //查询当前日期是否可预约
    OrderSetting getOrderSettingByData(Date date);

    //ordersetting+1
    void editResevationsByDate(OrderSetting orderSetting);
}

package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
            long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                orderSettingDao.editNumberByOrderDate(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String startDate = date + "-1";
        String endData = date + "-31";
        //这里使用bean 对象去接数据，亦可使用map去接，但需要在MYBATIS使用AS别名
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(startDate, endData);
        List<Map> dateList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            //获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());
            //可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());
            //已预约人数
            dateList.add(orderSettingMap);
        }
        return dateList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0 ) {
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}

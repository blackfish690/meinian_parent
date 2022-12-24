package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    //日期预约表
    @Autowired
    OrderSettingDao orderSettingDao;
    //会员表
    @Autowired
    MemberDao memberDao;
    //预约表
    @Autowired
    OrderDao orderDao;

    @Override
    public Result saveOrder(Map map) throws Exception {
        int setmealid = Integer.parseInt((String) map.get("setmealId"));
        Date date = DateUtils.parseString2Date((String) map.get("orderDate"));
        //1判断当前的日期是否可以预约(根orderDate查 ordersetting，能查询出来可以预约;查询不出来不能颅约)
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByData(date);
        if (orderSetting == null) { //orderSetting表中没有这个日期
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2判断当前日期是否预约已满(判断reservations(已经预约人数)是number (最多预约人数)
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.判断是否是会员(根据手机号码查t_member)
        String telephone = (String) map.get("telephone");
        Member member = memberDao.getMemberByPhone(telephone);
        if (member == null) {
            //如果不是会员(不能够查询出来)，自动注册为会员(直接向t_member插入一条记买)
            //快速注册
            member = new Member();
            member.setIdCard((String) map.get("idCard"));
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            memberDao.addMember(member);//此处需要主键回填
        } else {
            //查询是否重复预约，在t_order表查询member_id,orderDate,setmeal_id
            Order orderParam = new Order();
            orderParam.setMemberId(member.getId());
            orderParam.setOrderDate(date);
            orderParam.setSetmealId(setmealid);
            List<Order> orderList = orderDao.getOrderByParam(orderParam);
            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        // 4.进行预约
        try {
            //order表添加数据
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setOrderType(order.ORDERTYPE_WEIXIN);
            order.setOrderStatus(order.ORDERSTATUS_NO);
            order.setSetmealId(setmealid);
            orderDao.addOrder(order);//主键回填

            //orderSetting表reservations+1
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            orderSettingDao.editResevationsByDate(orderSetting);
            return new Result(true, MessageConstant.ORDER_SUCCESS, order);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

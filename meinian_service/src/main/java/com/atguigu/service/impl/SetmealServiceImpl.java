package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        setmealDao.addtSetmeal(setmeal);
        Integer setmealId = setmeal.getId();
        addSetMealTravelGroup(travelgroupIds, setmealId);
        //图品名称保存到redis
        String pic = setmeal.getImg();
        jedisPool.getResource().append(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    @Override
    public PageResult findPage(QueryPageBean param) {
        PageHelper.startPage(param.getCurrentPage(),param.getPageSize());
        Page page = setmealDao.findPage(param.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }


    private void addSetMealTravelGroup(Integer[] travelgroupIds, Integer setmealId) {
        if (travelgroupIds != null && travelgroupIds.length > 0) {
            for (Integer travelgroupId : travelgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelgroupId", travelgroupId);
                map.put("setmealId", setmealId);
                setmealDao.addSetMealTravelGroup(map);
            }
        }
    }
}

package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;

public interface SetmealDao {

    void addtSetmeal(Setmeal setmeal);

    void addSetMealTravelGroup(HashMap<String, Integer> map);

    Page findPage(String queryString);

    //查询所有数据
    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);
}

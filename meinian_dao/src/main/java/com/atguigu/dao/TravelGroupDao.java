package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void addGroupAndItem(Map map);

    Page<TravelGroup> findPage(@Param("queryString") String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelGroupAndTravelItem(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteByTravelGroupId(Integer id);

    Long selectCount(Integer id);

    void deleteTravelGroupById(Integer id);

    List<TravelGroup> findAll();

    List<TravelGroup> findTravelGroupListById(Integer id);
}

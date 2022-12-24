package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    TravelGroupDao travelgroupDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page page = travelgroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public TravelGroup findById(Integer id) {
        return travelgroupDao.findById(id);
    }

    @Override
    public List<Integer> findTravelGroupAndTravelItem(Integer id) {
        return travelgroupDao.findTravelGroupAndTravelItem(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelgroupDao.edit(travelGroup);
        travelgroupDao.deleteByTravelGroupId(travelGroup.getId());
        addGroupAndItem(travelItemIds,travelGroup.getId());
    }

    @Override
    public void deleteTravelGroupById(Integer id) {
        Long count = travelgroupDao.selectCount(id);
        if (count > 0) {
            throw new RuntimeException("不允许删除");
        }
        travelgroupDao.deleteTravelGroupById(id);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelgroupDao.findAll();
    }

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelgroupDao.add(travelGroup);
        Integer travelGroupId = travelGroup.getId();//主键回填
        addGroupAndItem(travelItemIds,travelGroupId);
    }

    private void addGroupAndItem(Integer[] travelItemIds, Integer travelGroupId) {
        for (Integer travelItemId : travelItemIds) {
            HashMap map = new HashMap<>();
            map.put("travelGroupId",travelGroupId);
            map.put("travelItemId",travelItemId);
            travelgroupDao.addGroupAndItem(map);
        }
    }
}

package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class) //发布服务,注册到zookeeper
@Transactional //声明式事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;

    //查询分页
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用分页插件
        //开启分页插件
        //算法是limit:(currentPage-1)*pageSize,pageSize
        PageHelper.startPage(currentPage,pageSize);
        Page page = travelItemDao.findPage(queryString);
        //(1.总记录数2.分页数据集合)
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        Long count = travelItemDao.selectCount(id);
        if (count > 0) {
            throw new RuntimeException("不允许删除");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

    //添加
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }
}

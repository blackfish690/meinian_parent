package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * ClearImaJob
 *
 * @Author: 马伟奇
 * @CreateTime: 2021-12-28
 * @Description:
 */
public class ClearImaJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片
     */
    public void clearImg() {
        // 返回其他集合与第一个集合的差异
        Set<String> set = jedisPool.getResource().sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES
        );
        // 迭代器
        Iterator<String> iterator = set.iterator();
        // 是否有下一个元素
        while (iterator.hasNext()) {
            String pic = iterator.next();
            System.out.println("删除图片的名字：" + pic);
            //删除七牛云里面的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除数据库
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
    }
}
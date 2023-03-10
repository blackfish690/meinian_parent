package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean param) {
        return setmealService.findPage(param);
    }


    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.add(travelgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            //1.获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //2.生成新的文件名(防止原源文件被覆盖)
            int index = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(index);
            String filename = UUID.randomUUID().toString() + suffix;
            //3.调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            //4.图片名称储存到redis
            jedisPool.getResource().append(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

}

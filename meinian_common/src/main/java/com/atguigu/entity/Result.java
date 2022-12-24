package com.atguigu.entity;

import java.io.Serializable;

/**
 * Result
 *
 * @Author: 马伟奇
 * @CreateTime: 2021-12-24
 * @Description:
 * 表示服务器返回给客户端的数据
 */
public class Result implements Serializable {
    // 封装一个成功或者失败
    // 表示服务器返回的数据是成功还是失败，true表示成功，false表示失败
    private boolean flag;
    // 表示服务器的友好提示
    private String message;
    // 表示服务器返回的数据
    private Object data;
    public Result(boolean flag, String message) {
        super();
        this.flag = flag;
        this.message = message;
    }

    public Result(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
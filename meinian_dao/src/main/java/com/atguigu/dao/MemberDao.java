package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {
    //根据手机号码查询会员
    Member getMemberByPhone(String telephone);

    //快速注册
    void addMember(Member member);
}

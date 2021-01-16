package com.vailter.standard.learn.chain.demo5.v2;

import com.vailter.standard.learn.chain.demo5.Member;

import java.util.Objects;

public class LoginHandler extends Handler{
    @Override
    void doHandler(Member member) {
        System.out.println("登陆成功");
        member.setRoleName("管理员");
        if (Objects.nonNull(next)) {
            next.doHandler(member);
        }
    }
}

package com.vailter.standard.learn.chain.demo5.v3;

import com.vailter.standard.learn.chain.demo5.Member;

import java.util.Objects;

public class LoginHandler extends Handler<Member> {
    @Override
    void doHandler(Member member) {
        System.out.println("登录成功");
        member.setRoleName("管理员");
        if (Objects.nonNull(next)) {
            next.doHandler(member);
        }
    }
}

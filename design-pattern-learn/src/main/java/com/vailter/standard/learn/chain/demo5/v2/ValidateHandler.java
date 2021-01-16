package com.vailter.standard.learn.chain.demo5.v2;

import com.vailter.standard.learn.chain.demo5.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ValidateHandler extends Handler{
    @Override
    void doHandler(Member member) {
        if (Objects.isNull(member) || StringUtils.isAnyBlank(member.getUserName(), member.getPassword())) {
            System.err.println("用户名或密码不能为空");
            return;
        }
        if (Objects.nonNull(next)) {
            next.doHandler(member);
        }
    }
}

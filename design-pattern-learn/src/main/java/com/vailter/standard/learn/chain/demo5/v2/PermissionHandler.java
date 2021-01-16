package com.vailter.standard.learn.chain.demo5.v2;

import com.vailter.standard.learn.chain.demo5.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class PermissionHandler extends Handler {
    @Override
    void doHandler(Member member) {
        if (!StringUtils.equals(member.getRoleName(), "管理员")) {
            System.err.println("你不是超级管理员，没有权限!");
            return;
        }

        System.out.println("继续操作");
        if (Objects.nonNull(next)) {
            next.doHandler(member);
        }
    }
}

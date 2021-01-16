package com.vailter.standard.learn.chain.demo5.v1;

import org.apache.commons.lang3.StringUtils;

public class MemberService {


    public void login(String userName, String password) {
        if (StringUtils.isAnyBlank(userName, password)) {
            System.err.println("用户名或密码不能为空");
            return;
        }

        boolean loginSuccess = doLogin(userName,password);

        if (!loginSuccess) {
            System.err.println("用户名或密码错误");
            return;
        }

        System.out.println("登录成功");

        if (!StringUtils.equals(userName, "管理员")) {
            System.err.println("你不是超级管理员，没有权限!");
            return;
        }

        System.out.println("继续操作");
    }

    private boolean doLogin(String userName, String password) {
        return (StringUtils.equals(userName, "管理员") && StringUtils.equals(password, "123456"))
                || (StringUtils.equals(userName, "test") && StringUtils.equals(password, "123456"));
    }
}

package com.vailter.standard.learn.chain.demo5.v2;

import com.vailter.standard.learn.chain.demo5.Member;
import org.apache.commons.lang3.StringUtils;

public class MemberService {


    public void login(String userName, String password) {
        Member member = Member.builder()
                .userName(userName)
                .password(password)
                .build();

        ValidateHandler validateHandler = new ValidateHandler();
        LoginHandler loginHandler = new LoginHandler();
        PermissionHandler permissionHandler = new PermissionHandler();
        validateHandler.next(loginHandler);
        loginHandler.next(permissionHandler);
        validateHandler.doHandler(member);
    }
}

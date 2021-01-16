package com.vailter.standard.learn.chain.demo5.v3;

import com.vailter.standard.learn.chain.demo5.Member;

public class MemberService {


    public void login(String userName, String password) {
        Member member = Member.builder()
                .userName(userName)
                .password(password)
                .build();

        Handler.Builder<Member> builder = new Handler.Builder<>();
        builder.addHandler(new ValidateHandler())
                .addHandler(new LoginHandler())
                .addHandler(new PermissionHandler())
                .build()
                .doHandler(member);
    }
}

package com.vailter.standard.learn.chain.demo5.v2;

public class LoginTest {
    public static void main(String[] args) {
        MemberService memberService = new MemberService();
        memberService.login("test","123");
    }
}

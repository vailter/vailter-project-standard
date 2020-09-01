package com.vailter.standard.netty.login;

import io.netty.util.AttributeKey;

public interface Attributes {
    /**
     * 登录状态标志
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}

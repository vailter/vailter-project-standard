package com.vailter.standard.netty.protocol;

/**
 * 指令集
 */
public interface Command {
    /**
     * 登录请求
     */
    Byte LOGIN_REQUEST = 1;
    /**
     * 登录响应
     */
    Byte LOGIN_RESPONSE = 2;
    /**
     * 消息请求
     */
    Byte MSG_REQUEST = 3;
    /**
     * 消息响应
     */
    Byte MSG_RESPONSE = 4;
}

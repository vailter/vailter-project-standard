package com.vailter.standard.netty.protocol;

import lombok.Data;

/**
 * 指令数据包
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     */
    abstract Byte getCommand();
}

package com.vailter.standard.netty.protocol;

import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {
    private Integer userId;

    private String username;

    private String password;

    @Override
    Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}

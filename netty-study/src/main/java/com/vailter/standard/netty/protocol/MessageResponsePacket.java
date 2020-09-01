package com.vailter.standard.netty.protocol;

import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    Byte getCommand() {
        return Command.MSG_RESPONSE;
    }
}

package com.vailter.standard.netty.protocol;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

@Data
public class LoginResponsePacket extends Packet {
    private Boolean success;
    private String reason;

    @Override
    Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }

    public boolean isSuccess() {
        return BooleanUtils.isTrue(success);
    }
}

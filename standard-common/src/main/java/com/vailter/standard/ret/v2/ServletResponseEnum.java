package com.vailter.standard.ret.v2;

import com.vailter.standard.exception.v2.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServletResponseEnum implements BusinessExceptionAssert {
    SERVER_ERROR(500, "unKnow Exception.");

    /**
     * 返回码
     */
    private final int code;
    /**
     * 返回消息
     */
    private final String message;
}

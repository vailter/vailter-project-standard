package com.vailter.standard.exception.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private int code;
    private String message;

    private IResponseEnum responseEnum;

    public BaseException(IResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
        this.responseEnum = responseEnum;
    }

    public BaseException(IResponseEnum responseEnum, String message, Throwable cause) {
        super(message, cause);
        this.code = responseEnum.getCode();
        this.message = message;
        this.responseEnum = responseEnum;
    }

    public BaseException(IResponseEnum iResponseEnum) {
        super(iResponseEnum.getMessage());
        this.responseEnum = iResponseEnum;
        this.code = responseEnum.getCode();
        this.message = iResponseEnum.getMessage();
    }
}

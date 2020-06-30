package com.vailter.standard.exception.v2;

import java.text.MessageFormat;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    public BusinessException(IResponseEnum responseEnum, String message, Throwable cause) {
        super(responseEnum, message, cause);
    }
}

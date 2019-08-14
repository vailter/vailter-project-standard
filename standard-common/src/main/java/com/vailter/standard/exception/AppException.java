package com.vailter.standard.exception;

import com.vailter.standard.ret.CodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Vailter67
 */
@Getter
@Setter
public class AppException extends RuntimeException {
    private CodeMsg cm;

    public AppException() {
        super();
    }

    public AppException(CodeMsg cm) {
        super(cm.getMsg());
        this.cm = cm;
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    protected AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

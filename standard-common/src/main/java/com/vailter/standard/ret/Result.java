package com.vailter.standard.ret;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * @author taoshiyun
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功时候的调用
     */
    public static <T> Result<T> success(T data) {
        return Objects.nonNull(data) ? new Result<>(data) : new Result<>();
    }

    /**
     * 失败时候的调用
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<>(cm);
    }

    private Result() {
        this.code = CodeMsg.SUCCESS.getCode();
        this.msg = CodeMsg.SUCCESS.getMsg();
    }

    private Result(T data) {
        this.code = CodeMsg.SUCCESS.getCode();
        this.msg = CodeMsg.SUCCESS.getMsg();
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if (cm == null) {
            throw new IllegalArgumentException("CodeMsg不能为空！");
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }
}
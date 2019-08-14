package com.vailter.standard.ret;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vailter.standard.ret.CodeMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

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
        return new Result<>(data);
    }

    /**
     * 失败时候的调用
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<>(cm);
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
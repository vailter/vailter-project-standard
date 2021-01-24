package com.vailter.standard.ret.v2;

import com.vailter.standard.exception.v2.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {
 * # 返回状态码
 * code:integer,
 * # 返回信息描述
 * message:string,
 * # 返回值
 * data:object
 * }
 * <p>
 * 下面是常见的HTTP状态码：
 * 200 - 请求成功
 * 301 - 资源（网页等）被永久转移到其它URL
 * 404 - 请求的资源（网页等）不存在
 * 500 - 内部服务器错误
 * <p>
 * # 1000～1999 区间表示参数错误
 * # 2000～2999 区间表示用户错误
 * # 3000～3999 区间表示接口异常
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements BusinessExceptionAssert {

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

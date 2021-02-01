package com.vailter.standard.ret;

import lombok.Getter;

/**
 * 不需要set方法，不让外界修改
 *
 * @author V
 */
@Getter
public class CodeMsg {
    /**
     * 成功
     */
    public static final CodeMsg SUCCESS = new CodeMsg(0, "成功");

    /**
     * 通用异常 400-500
     */
    public static final CodeMsg BIND_ERROR = new CodeMsg(400, "参数校验异常：%s");
    public static final CodeMsg PERMISSION_CHECK_ERROR = new CodeMsg(10003, "权限校验失败，请检查账号");
    public static final CodeMsg NOT_SUPPORTED_HTTP_METHOD = new CodeMsg(405, "不支持的调用类型[%s]，请使用%s请求");
    public static final CodeMsg NOT_SUPPORTED_CONTENT_TYPE = new CodeMsg(406, "不支持的Content-Type: [%s]，请使用：%s");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500, "服务端异常");

    /**
     * 授信 200XX
     */
    public static final CodeMsg RULES_ERROR = new CodeMsg(20010, "规则异常");

    /**
     * 授信 201XX
     */
    public static final CodeMsg IN_EXIST_CREDIT_REPORT = new CodeMsg(20110, "授信结果查询流水不存在");
    public static final CodeMsg CREDIT_PROCESSING = new CodeMsg(20111, "授信处理中");
    public static final CodeMsg EXIST_CREDIT_APPLY = new CodeMsg(20112, "授信已申请过");
    public static final CodeMsg PUSH_CREDIT_REPORT_ERROR = new CodeMsg(20113, "推送授信异常");

    /**
     * 借款 202XX
     */
    public static final CodeMsg IN_EXIST_LOAN_APPLY = new CodeMsg(20200, "借款订单编号不存在");
    public static final CodeMsg IN_EXIST_SELLER_CREDIT_APPLY = new CodeMsg(20201, "柜员认证流水号不存在");

    /**
     * Api 301XX
     */
    public static final CodeMsg NON_REPORT_BY_ID_CARD = new CodeMsg(30100, "未获取到同盾报告编号");
    /**
     * 合同打标签
     */
    public static final CodeMsg CONTRACT_MARK_REHEAR_REPORT_NOT_EXSIT = new CodeMsg(60112,"合同打标签,复审报告或电核报告不存在");
    public static final CodeMsg PUSH_CONTRACT_MARK_ERROR = new CodeMsg(60113,"推送合同打标签报告异常");

    /**
     * 10000	未知错误
     * 10001	参数不全
     * 10002	参数格式不正确
     * 10003	用户名密码出错
     * 20013	不存在该编号
     * 20014	报告尚未生成
     */
    public static final CodeMsg UNKNOWN_ERROR = new CodeMsg(10000, "未知错误");
    public static final CodeMsg PARAMS_NOT_COMPLETE = new CodeMsg(10001, "参数不全");
    public static final CodeMsg PARAMS_FORMAT_ERROR = new CodeMsg(10002, "参数格式不正确");
    public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(10003, "接口访问受限");;
    public static final CodeMsg IN_EXIST_CREDIT_APPLY = new CodeMsg(20013, "授信订单编号不存在该编号");
    public static final CodeMsg CREDIT_REPORT_CREATING = new CodeMsg(20014, "授信报告尚未生成");

    private int code;
    private String msg;

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }
}

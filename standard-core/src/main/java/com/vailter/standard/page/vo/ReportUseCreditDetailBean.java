package com.vailter.standard.page.vo;

import com.vailter.standard.utils.desensitization.Desensitized;
import com.vailter.standard.utils.desensitization.SensitiveTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportUseCreditDetailBean implements Serializable {

    private String statisticDate;
    //省份
    private String provinceName;
    //地市
    private String cityName;
    //区域
    private String areaName;
    //姓名
    @Desensitized(type = SensitiveTypeEnum.CHINESE_NAME)
    private String realName;
    //手机号
    @Desensitized(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String borrowerPhone;

    //业务员姓名
    //@Desensitized(type = SensitiveTypeEnum.CHINESE_NAME)
    private String oprName;
    //业务员编号
    //@Desensitized(type = SensitiveTypeEnum.WORK_NO)
    private String oprCode;
    //业务员手机号
    //@Desensitized(type = SensitiveTypeEnum.MOBILE_PHONE)
    private String oprPhone;
    //营业厅编号
    private String depCode;
    //营业厅名称
    private String depName;
    //营业厅模式
    private String depModel;
    //引流方订单号
    private String drainageOrderNo;
    //引流方订单日期
    private String drainageOrderDate;
    //引流方套餐编号
    private String drainagePackageCode;
    //引流方套餐描述
    private String drainagePackageDesp;
    //商品名称
    private String goodsName;
    //贷款金额
    private BigDecimal loanAmt;
    //分期数
    private Integer period;
    //套餐办理完成时间
    private String packageFinishTime;
    //套餐确认时间
    private String packageConfirmTime;
    //资金方
    private String debit;
    private String status;
}

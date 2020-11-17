package com.vailter.standard.page.dto;


import lombok.Data;

@Data
public class ReportNuggetDto {

    //省份ID
    private Integer provinceId;
    //省份名称
    private String provinceName;
    //地市ID
    private Integer cityId;
    //地市名称
    private String cityName;

    private Integer regionCode;

    private String areaName;

    private String type;


    private String startDate;

    private String endDate;

    private String field;

    private String subProductType;

    private Integer periods;
}

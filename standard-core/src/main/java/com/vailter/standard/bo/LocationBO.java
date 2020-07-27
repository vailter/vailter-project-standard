package com.vailter.standard.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationBO {
    /**
     * 省份
     */
    private String province;

    private String shortProvince;

    /**
     * 市
     */
    private String city;

    private String shortCity;
    /**
     * 区县
     */
    private String county;

    private String shortCounty;
    /**
     * 详细地址-乡镇
     */
    private String town;

    private String detail;

    private String detailFormat;

    public LocationBO(String shortProvince, String shortCity) {
        this.shortProvince = shortProvince;
        this.shortCity = shortCity;
    }
}

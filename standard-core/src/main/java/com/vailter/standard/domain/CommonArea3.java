package com.vailter.standard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`common_area3`")
public class CommonArea3 {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 父级编号
     */
    @Column(name = "`parent_code`")
    private String parentCode;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 行政区划码
     */
    @Column(name = "`xz_code`")
    private String xzCode;

    /**
     * 省
     */
    @Column(name = "`province`")
    private String province;

    /**
     * 市
     */
    @Column(name = "`city`")
    private String city;

    /**
     * 县
     */
    @Column(name = "`county`")
    private String county;

    /**
     * 区号
     */
    @Column(name = "`area_code`")
    private String areaCode;

    /**
     * 邮编
     */
    @Column(name = "`zip_code`")
    private String zipCode;

    /**
     * 简称
     */
    @Column(name = "`short_name`")
    private String shortName;

    /**
     * 全称拼音
     */
    @Column(name = "`full_pin`")
    private String fullPin;

    /**
     * 简拼
     */
    @Column(name = "`short_pin`")
    private String shortPin;

    /**
     * 首字母
     */
    @Column(name = "`first_pin`")
    private String firstPin;

    /**
     * 英文
     */
    @Column(name = "`english_name`")
    private String englishName;

    /**
     * 经度
     */
    @Column(name = "`lng`")
    private Double lng;

    /**
     * 纬度
     */
    @Column(name = "`lat`")
    private Double lat;

    /**
     * 省简称
     */
    @Column(name = "`province_short_name`")
    private String provinceShortName;

    /**
     * 市简称
     */
    @Column(name = "`city_short_name`")
    private String cityShortName;

    /**
     * 县简称
     */
    @Column(name = "`county_short_name`")
    private String countyShortName;

    /**
     * 省拼音
     */
    @Column(name = "`province_pin`")
    private String provincePin;

    /**
     * 市拼音
     */
    @Column(name = "`city_pin`")
    private String cityPin;

    /**
     * 县拼音
     */
    @Column(name = "`county_pin`")
    private String countyPin;

    /**
     * 深度
     */
    @Column(name = "`level`")
    private Boolean level;

    /**
     * 编号路径
     */
    @Column(name = "`code_path`")
    private String codePath;

    /**
     * 全路径名称
     */
    @Column(name = "`merge_name`")
    private String mergeName;
}
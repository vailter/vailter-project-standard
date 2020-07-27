package com.vailter.standard.service;

import com.vailter.standard.bo.LocationBO;
import com.vailter.standard.mapper.CommonArea3Mapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式化地址
 */
@Service
@Slf4j
public class AddressFormatService {

    @Resource
    private CommonArea3Mapper commonArea3Mapper;

    /**
     * 省级后缀
     */
    private static final String[] PROVINCE_SUFFIXES = {"省", "市", "自治区", "特别行政区"};

    /**
     * 直辖市
     */
    private static final String[] MUNICIPALITIES = {"北京", "天津", "上海", "重庆", "北京市", "天津市", "上海市", "重庆市"};

    /**
     * 特别行政区
     */
    private static final String[] SPECIAL_ADMINISTRATIVE_REGIONS = {"香港", "澳门"};

    /**
     * 自治区
     */
    private static final String[] AUTONOMOUS_REGIONS = {"广西", "内蒙古", "西藏", "宁夏", "新疆"};

    /**
     * 区域的正则
     */
    private static final String AREA_REGEX = "(?<province>[^特别行政区]+特别行政区|[^自治区]+自治区|[^省]+省|[^市]+市)(?<city>省直辖行政单位|省属虚拟市|市辖县|市辖区|县|自治州|[^地区]+地区|[^州]+州|[^盟]+盟|[^市]+市|[^区]+区|)?(?<county>[^旗]+旗|[^市]+市|[^区]+区|[^县]+县)?(?<town>[^县]+县|[^区]+区|[^乡]+乡|[^村]+村|[^镇]+镇|[^街道]+街道)?(?<village>.*)";

    /**
     * 所有省份
     */
    private List<Map<String, String>> allProvinceMapList;
    /**
     * 所有城市
     */
    private List<Map<String, String>> allCityMapList;
    /**
     * 所有县级
     */
    private List<Map<String, String>> allCountyMapList;

    @PostConstruct
    private void init() {
        // 获取所有省份
        this.allProvinceMapList = commonArea3Mapper.queryByNameAndLevel(null, 1);

        // 获取所有城市
        this.allCityMapList = commonArea3Mapper.queryByNameAndLevel(null, 2);

        // 获取所有县级
        this.allCountyMapList = commonArea3Mapper.queryByNameAndLevel(null, 3);
    }

    /**
     * 出现下列关键词的将不作处理
     */
    @SuppressWarnings({"serial"})
    private static final Map<Integer, String[]> cityKeyWords = new LinkedHashMap<Integer, String[]>() {{
        put(0, new String[]{"县"});
        put(1, new String[]{"市辖区", "市辖县"});
        put(2, new String[]{"盟", "州", "地区", "自治州", "回族自治州", "土家族苗族自治州", "藏族自治州", "藏族羌族自治州", "蒙古族藏族自治州", "壮族苗族自治州", "傣族自治州", "彝族自治州", "朝鲜族自治州", "布依族苗族自治州", "苗族侗族自治州", "傣族景颇族自治州", "傈僳族自治州", "白族自治州", "哈尼族彝族自治州"});
    }};

    /**
     * 格式化省市县/区信息
     *
     * @param address
     * @return
     */
    public LocationBO format(String address) {
        if (StringUtils.isBlank(address)) {
            return null;
        }

        // 去除空格
        address = StringUtils.replaceAll(address, "\\s+", "");

        // 把全部的 "市辖区" "市辖县" 替换成 ""
        address = StringUtils.replaceEach(address, cityKeyWords.get(1), new String[]{"", ""});
        System.out.println(address);

        // 推导省份
        Map<String, String> provinceMap = inferProvince(address);
        LocationBO locationBO;
        // 县级后缀
        String countyUnit = cityKeyWords.get(0)[0];
        // 省份未知，根据城市或者县区推导
        if (MapUtils.isEmpty(provinceMap)) {
            // 如果第一位是 "县"，替换为 ""
            if (StringUtils.startsWith(address, countyUnit)) {
                address = StringUtils.replace(address, countyUnit, "");
            }
            locationBO = resolveByCityWithNullProvince(address);
            if (Objects.nonNull(locationBO)) {
                return locationBO;
            }
            return resolveByCountyWithNullProvAndNullCity(address);
        }
        String province = provinceMap.get("name");
        String shortProvince = provinceMap.get("short_name");

        // 重构地区格式（替换为省级全称）
        address = restructure(address, province, shortProvince);

        // 去掉省份后的地址
        address = StringUtils.replaceAll(address, province, "");

        // 后面无地址，直接返回
        if (StringUtils.isBlank(address)) {
            return resolveAddress(province, shortProvince, null, null);
        }

        // 如果第一位是 "县"，替换为 ""
        if (StringUtils.startsWith(address, countyUnit)) {
            address = StringUtils.replace(address, countyUnit, "");
        }

        // 如果 省份 是 直辖市，自动插入 "市辖区" 用于区分
        if (StringUtils.equalsAny(shortProvince, MUNICIPALITIES)) {
            address = province + cityKeyWords.get(1)[0] + address;
            return resolveAddress(address, shortProvince, shortProvince, null);
        }
        // 通过城市处理
        locationBO = resolveByCityWithNonNullProv(address, province, shortProvince);
        if (Objects.nonNull(locationBO)) {
            return locationBO;
        }
        // 通过县处理
        return resolveByCountyWithNonNullProvAndNullCity(address, province, shortProvince);
    }

    /**
     * 推断省份
     *
     * @param address 地址全路径
     * @return
     */
    private Map<String, String> inferProvince(String address) {
        return allProvinceMapList.parallelStream()
                .filter(provinceMap -> address.indexOf(provinceMap.get("short_name")) == 0)
                .findAny()
                .orElse(null);
    }

    /**
     * 推断城市
     *
     * @param address 地址全路径
     * @return
     */
    private Map<String, String> inferCity(String address) {
        return allCityMapList.parallelStream()
                .filter(cityMap -> address.indexOf(cityMap.get("short_name")) == 0)
                .findAny()
                .orElse(null);
    }

    /**
     * 推断县
     *
     * @param address 地址全路径
     * @return
     */
    private Map<String, String> inferCounty(String address) {
        return allCountyMapList.parallelStream()
                .filter(countyMap -> address.indexOf(countyMap.get("short_name")) == 0)
                .findAny()
                .orElse(null);
    }

    /**
     * 通过解析城市处理未知省份的地址
     *
     * @param address 地址全路径
     * @return
     */
    private LocationBO resolveByCityWithNullProvince(String address) {
        Map<String, String> cityMap = inferCity(address);
        if (MapUtils.isNotEmpty(cityMap)) {
            String city = cityMap.get("name");
            String shortCity = cityMap.get("short_name");
            // 格式化地址（拼接 “市” 后缀）
            address = restructure(address, city, shortCity);
            String province = cityMap.get("province");
            String shortProvince = cityMap.get("province_short_name");

            // 如果是直辖市
            if (StringUtils.equalsAny(province, MUNICIPALITIES)) {
                // 拼接全称地址
                address = province + cityKeyWords.get(1)[0] + address;
                return resolveAddress(address, shortProvince, shortCity, null);
            }
            // 拼接全称地址
            address = province + address;
            return resolveAddress(address, shortProvince, shortCity, null);
        }
        return null;
    }

    /**
     * 通过解析县级处理未知省份和未知城市的地址
     *
     * @param address 地址全路径
     * @return
     */
    private LocationBO resolveByCountyWithNullProvAndNullCity(String address) {
        Map<String, String> countyMap = inferCounty(address);
        if (MapUtils.isEmpty(countyMap)) {
            return resolveAddress(address, null, null, null);
        }
        String county = countyMap.get("name");
        String shortCounty = countyMap.get("short_name");
        // 格式化地址（拼接 “县” 后缀）
        String tempAddress = restructure(address, county, shortCounty);
        // 无效的县
        if (StringUtils.isBlank(tempAddress)) {
            return resolveAddress(address, null, null, null);
        }
        address = tempAddress;
        String province = countyMap.get("province");
        String shortProvince = countyMap.get("province_short_name");
        String city = countyMap.get("city");
        String shortCity = countyMap.get("city_short_name");
        // 拼接全称地址
        address = province + city + address;
        return resolveAddress(address, shortProvince, shortCity, shortCounty);
    }

    /**
     * 通过解析县级处理已知省份未知城市的地址
     *
     * @param addressWithNullProvince 不包含省份的地址
     * @param province
     * @param shortProvince
     * @return
     */
    private LocationBO resolveByCountyWithNonNullProvAndNullCity(String addressWithNullProvince, String province, String shortProvince) {
        String address,
                city = null,
                shortCity = null,
                shortCounty = null,
                tempAddress = null;
        List<Map<String, String>> counties = commonArea3Mapper.queryByNameAndLevel(shortProvince, 3);
        if (CollectionUtils.isEmpty(counties)) {
            address = province + addressWithNullProvince;
            return resolveAddress(address, shortProvince, null, null);
        }

        for (Map<String, String> countyMap : counties) {
            String county = countyMap.get("name");
            shortCounty = countyMap.get("short_name");
            // 格式化地址（拼接 “县” 后缀）
            tempAddress = restructure(addressWithNullProvince, county, shortCounty);

            if (StringUtils.isNotBlank(tempAddress)) {
                city = countyMap.get("city");
                shortCity = countyMap.get("city_short_name");
                addressWithNullProvince = tempAddress;
                break;
            }
        }
        // 匹配到县级
        if (StringUtils.isNotBlank(tempAddress)) {
            // 拼接全称地址
            address = province + city + addressWithNullProvince;
            return resolveAddress(address, shortProvince, shortCity, shortCounty);
        }
        // 未匹配到县级
        address = province + addressWithNullProvince;
        return resolveAddress(address, shortProvince, null, null);
    }

    /**
     * 通过解析城市处理已知省份的地址
     *
     * @param addressWithNullProvince 不包含省份的地址
     * @param province
     * @param shortProvince
     * @return
     */
    private LocationBO resolveByCityWithNonNullProv(String addressWithNullProvince, String province, String shortProvince) {
        String address, city, shortCity = null, tempAddress = null;
        // 获取当前省份的城市
        List<Map<String, String>> cities = commonArea3Mapper.queryByNameAndLevel(shortProvince, 2);
        if (CollectionUtils.isNotEmpty(cities)) {
            for (Map<String, String> cityMap : cities) {
                city = cityMap.get("name");
                shortCity = cityMap.get("short_name");
                // 格式化地址（替换为“市”全称）
                tempAddress = restructure(addressWithNullProvince, city, shortCity);
                if (StringUtils.isNotBlank(tempAddress)) {
                    addressWithNullProvince = tempAddress;
                    break;
                }
            }
        }
        // 匹配到市
        if (StringUtils.isNotBlank(tempAddress)) {
            // 拼接全称地址
            address = province + addressWithNullProvince;
            return resolveAddress(address, shortProvince, shortCity, null);
        }
        return null;
    }

    /**
     * 推导省份后缀
     *
     * @param province
     * @return
     */
    private String inferProvinceSuffix(String province) {
        // 直辖市
        if (ArrayUtils.contains(MUNICIPALITIES, province)) {
            return PROVINCE_SUFFIXES[1];
        }

        // 特别行政区
        if (ArrayUtils.contains(SPECIAL_ADMINISTRATIVE_REGIONS, province)) {
            return PROVINCE_SUFFIXES[3];
        }

        // 自治区
        if (ArrayUtils.contains(AUTONOMOUS_REGIONS, province)) {
            String suffix = PROVINCE_SUFFIXES[2];
            switch (province) {
                case "广西":
                    return "壮族" + suffix;
                case "宁夏":
                    return "回族" + suffix;
                case "新疆":
                    return "维吾尔" + suffix;
                default:
                    return suffix;
            }
        }
        // 省
        return PROVINCE_SUFFIXES[0];
    }

    /**
     * 拼接“省”，“市”后缀
     *
     * @param address
     * @param fullPath
     * @param shortPath
     * @return
     */
    private static String restructure(String address, String fullPath, String shortPath) {
        if (StringUtils.indexOf(address, fullPath) == 0) {
            return address;
        } else if (StringUtils.indexOf(address, shortPath) == 0) {
            return StringUtils.replaceFirst(address, shortPath, fullPath);
        }
        return null;
    }


    /**
     * 解析地址
     *
     * @param address
     * @return
     */
    public LocationBO resolveAddress(String address, String shortProvince, String shortCity, String shortCounty) {
        LocationBO.LocationBOBuilder locationBOBuilder = LocationBO.builder();
        locationBOBuilder.shortProvince(shortProvince);
        locationBOBuilder.shortCity(shortCity);
        locationBOBuilder.shortCounty(shortCounty);
        Matcher matcher = Pattern.compile(AREA_REGEX).matcher(address);
        while (matcher.find()) {
            locationBOBuilder.province(writeNullStrAsEmpty(matcher.group("province")));
            locationBOBuilder.city(writeNullStrAsEmpty(matcher.group("city")));
            locationBOBuilder.county(writeNullStrAsEmpty(matcher.group("county")));
            locationBOBuilder.town(writeNullStrAsEmpty(matcher.group("town")) + writeNullStrAsEmpty(matcher.group("village")));
        }
        //重构一次地址
        return resolveAddress(locationBOBuilder.build());
    }

    /**
     * 重构一次地址，将直辖市所在区域进行特殊处理：将解析后的地址进行去重，去除掉重复的省市区
     * 注：如果在地址中出现未明确省市区的将无法去重，由于详细地址中可能出现于省市同名的情况，所有对于这类情况将
     * 保留，即使从肉眼能看出是重复的，也不会处理
     * 例如：四川省成都市高新区四川成都高新xxxx大道xxx号
     *
     * @param locationBO
     * @return
     */
    private LocationBO resolveAddress(LocationBO locationBO) {
        String province = locationBO.getProvince();
        String shortProvince = locationBO.getShortProvince();
        String city = locationBO.getCity();

        // 针对直辖市，进行特殊处理
        // 将直辖市-市级全称全部替换为区级内容，并将区级内容全部替换为""
        // 将直辖市-市级简称全部替换为省份简称
        if (StringUtils.equals(city, cityKeyWords.get(1)[0])) {
            locationBO.setCity(locationBO.getCounty());
            locationBO.setShortCity(shortProvince);
            locationBO.setCounty("");
            locationBO.setShortCounty("");
        }

        // 市
        city = locationBO.getCity();
        // 区（县）
        String county = locationBO.getCounty();

        // 街道，乡村，镇
        String town = locationBO.getTown();
        if (StringUtils.isNotBlank(town)) {
            town = StringUtils.replace(town, city, "");
            town = StringUtils.replace(town, county, "");
            town = StringUtils.replace(town, province, "");
            locationBO.setTown(town);
        }

        StringBuilder detailAddress = new StringBuilder();
        detailAddress.append(province);
        if (StringUtils.isNotBlank(city)) {
            detailAddress.append(",").append(city);
        }
        if (StringUtils.isNotBlank(county)) {
            detailAddress.append(",").append(county);
        }
        if (StringUtils.isNotBlank(town)) {
            detailAddress.append(",").append(town);
        }
        locationBO.setDetail(detailAddress.toString().replaceAll(",", ""));
        locationBO.setDetailFormat(detailAddress.toString());
        return locationBO;
    }

    /**
     * 将 null值 改为 ""
     *
     * @param val
     * @return
     */
    private String writeNullStrAsEmpty(String val) {
        if (null == val) {
            return "";
        }
        return val;
    }
}

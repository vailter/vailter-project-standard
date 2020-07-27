package com.vailter.standard.mapper;

import com.vailter.standard.domain.CommonArea3;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface CommonArea3Mapper extends Mapper<CommonArea3> {
    List<Map<String, String>> queryByNameAndLevel(@Param("shortProvince") String shortProvince, @Param("level") Integer level);
}
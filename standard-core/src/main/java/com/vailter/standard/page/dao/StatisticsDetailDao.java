package com.vailter.standard.page.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.vailter.standard.page.dto.ReportNuggetDto;
import com.vailter.standard.page.vo.ReportUseCreditDetailBean;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface StatisticsDetailDao extends Mapper<ReportUseCreditDetailBean> {
    @DS("hbnuggets")
    List<ReportUseCreditDetailBean> nuggetsDetail(ReportNuggetDto reportNuggetDto);
}

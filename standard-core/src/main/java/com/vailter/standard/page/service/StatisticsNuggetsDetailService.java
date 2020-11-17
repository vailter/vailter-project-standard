package com.vailter.standard.page.service;

import com.vailter.standard.page.dao.StatisticsDetailDao;
import com.vailter.standard.page.dto.ReportNuggetDto;
import com.vailter.standard.page.vo.ReportUseCreditDetailBean;
import com.vailter.standard.pager.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsNuggetsDetailService implements IBaseService<ReportNuggetDto, ReportUseCreditDetailBean> {
    @Autowired
    private StatisticsDetailDao statisticsDetailDao;

    @Override
    public List<ReportUseCreditDetailBean> list(ReportNuggetDto param) {
        return statisticsDetailDao.nuggetsDetail(param);
    }
}

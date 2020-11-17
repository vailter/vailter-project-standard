package com.vailter.standard.controller;

import com.github.pagehelper.PageInfo;
import com.vailter.standard.page.dto.ReportNuggetDto;
import com.vailter.standard.page.service.StatisticsNuggetsDetailService;
import com.vailter.standard.page.vo.ReportUseCreditDetailBean;
import com.vailter.standard.pager.dto.PageParam;
import com.vailter.standard.ret.v2.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("page")
public class PageAndExportController {

    @Autowired
    private StatisticsNuggetsDetailService statisticsNuggetsDetailService;

    @GetMapping
    public String page() {
        return "page/list";
    }

    @GetMapping("query")
    @ResponseBody
    public PageUtils query() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("index", i + 1);
            item.put("name", "test" + i);
            item.put("age", i + ThreadLocalRandom.current().nextInt(18));
            list.add(item);
        }
        return new PageUtils(list.size(), list);
    }

    @PostMapping("/nuggetsDetail")
    @ResponseBody
    public PageUtils nuggetsDetail(@RequestBody PageParam<ReportNuggetDto> pageParam) {
        Integer pageSize = pageParam.getPageSize();
        // 导出
        if (Objects.isNull(pageSize)) {
            List<ReportUseCreditDetailBean> list = statisticsNuggetsDetailService.listByPage(pageParam.getParam());
            return new PageUtils(list.size(), list);
        }
        PageInfo<ReportUseCreditDetailBean> pageInfo = statisticsNuggetsDetailService.page(pageParam);
        return new PageUtils(Math.toIntExact(pageInfo.getTotal()), pageInfo.getList());
    }
}

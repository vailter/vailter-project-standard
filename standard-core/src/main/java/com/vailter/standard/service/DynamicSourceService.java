package com.vailter.standard.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DynamicSourceService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @DS("scheduler")
    public void testDynamicDatasource() {
        test();
    }

    private void test() {
        int count = jdbcTemplate.queryForObject("select count(1) from schedule_project", Integer.class);
        System.out.println(count);
    }
}

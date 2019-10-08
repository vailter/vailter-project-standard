package com.vailter.standard;

import com.vailter.standard.datasource.dynamic.annotation.DataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@DisplayName("jdbc测试")
@SpringBootTest
class JdbcTest {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void testQuery() {
        String reqId = "tsy001";
        //jdbcTemplate.update("insert into biocloo_workdata_sub_info(req_id) values(?)", reqId);
        //System.out.println(1/0);

        //jdbcTemplate.update("delete from USER where NAME = ?", name);

        //jdbcTemplate.update("delete from USER");

        int count = jdbcTemplate.queryForObject("select count(1) from biocloo_workdata_sub_info", Integer.class);
        System.out.println(count);

        RowMapper rowMapper = new ColumnMapRowMapper(); // map 类型

        rowMapper = new BeanPropertyRowMapper(SubInfo.class); // entity 类型

        List<SubInfo> list = jdbcTemplate.query("select * from biocloo_workdata_sub_info", rowMapper);
        System.out.println(list);
    }

    @Test
    @DataSource("fintech")
    void testDynamicDatasource() {
        int count = jdbcTemplate.queryForObject("select count(1) from biocloo_workdata_sub_info", Integer.class);
        System.out.println(count);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    private static class SubInfo {
        private Long id;
        private String reqId;
        private Date createTime;
    }
}

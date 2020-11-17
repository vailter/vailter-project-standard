package com.vailter.standard;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.vailter.standard.learn.responsibilitychain.pipelinewithspring.ApplicationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;

/**
 * @author Vailter67
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
//@EnableHasor()
//@EnableHasorWeb()
@MapperScan(basePackages = {"com.vailter.**.mapper", "com.vailter.**.dao"})
@EnableConfigurationProperties(ZengpinlinProperties.class)
public class StandardApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StandardApplication.class, args);
    }


    @Resource
    private ApplicationService applicationService;

    @Override
    public void run(String... args) throws Exception {
        // 责任链应用实例
        applicationService.mockedClient();
    }
}

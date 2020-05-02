package com.vailter.standard;

import com.vailter.standard.learn.responsibilitychain.pipelinewithspring.ApplicationService;
import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author Vailter67
 */
@SpringBootApplication
@EnableHasor()
@EnableHasorWeb()
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

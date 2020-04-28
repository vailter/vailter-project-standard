package com.vailter.standard;

import com.vailter.standard.learn.responsibilitychain.pipelinewithspring.ApplicationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author Vailter67
 */
@SpringBootApplication
public class StandardApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StandardApplication.class, args);
    }


    @Resource
    private ApplicationService applicationService;

    @Override
    public void run(String... args) throws Exception {
        applicationService.mockedClient();
    }
}

package com.vailter.standard.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 定义了一个读取配置文件的工具类 CustomResourceReader
 *
 * @author Vailter67
 */
public class CustomResourceReader {
    public Properties readResource(String location) {
        return loadProperties(location);
    }

    public static Properties staticReadResource(String location) {
        return loadProperties(location);
    }

    public static CustomConfig customConfig(String location) {
        Properties properties = loadProperties(location);
        String env = properties.getProperty("spring.profiles.active");
        return new CustomConfig(env);
    }

    public static Properties loadProperties(String location) {
        Resource resource = new ClassPathResource(location);
        Properties properties = new Properties();
        try {
            InputStream in = resource.getInputStream();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomConfig {
        private String env;
    }
}

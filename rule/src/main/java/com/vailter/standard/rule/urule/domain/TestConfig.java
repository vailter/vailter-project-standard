package com.vailter.standard.rule.urule.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

/**
 * 如果 application.properties/yml也配置了 会被覆盖
 */
@Data
@Component
@ConfigurationProperties(prefix = "test")
@PropertySources(value = {@PropertySource("classpath:test.properties")})
public class TestConfig {
    private String name;
    private String type;
}

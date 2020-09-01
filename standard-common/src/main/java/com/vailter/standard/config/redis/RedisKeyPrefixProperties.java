package com.vailter.standard.config.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.redis.prefix")
public class RedisKeyPrefixProperties {
    private Boolean enable = Boolean.TRUE;
    private String key;
}

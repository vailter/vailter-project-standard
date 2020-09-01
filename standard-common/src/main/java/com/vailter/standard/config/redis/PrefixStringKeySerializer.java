package com.vailter.standard.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 对字符串序列化新增前缀
 */
public class PrefixStringKeySerializer extends StringRedisSerializer {
    private final Charset charset = StandardCharsets.UTF_8;
    private final RedisKeyPrefixProperties prefix;

    public PrefixStringKeySerializer(RedisKeyPrefixProperties prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        String keyPrefix = "";
        String keySuffix = bytes == null ? null : new String(bytes, charset);
        if (prefix.getEnable() != null && prefix.getEnable()) {
            keyPrefix = spliceKey(prefix.getKey());
        }
        return keyPrefix + keySuffix;
    }

    @Override
    public byte[] serialize(@Nullable String key) {
        if (prefix.getEnable() != null && prefix.getEnable()) {
            key = spliceKey(prefix.getKey()) + key;
        }
        System.out.println(key);
        return key == null ? null : key.getBytes(charset);
    }

    private String spliceKey(String prefixKey) {
        if (StringUtils.isBlank(prefixKey)) {
            return "";
        }
        if (!StringUtils.endsWith(prefixKey, ":")) {
            prefixKey = prefixKey + ":";
        }
        return prefixKey;
    }
}

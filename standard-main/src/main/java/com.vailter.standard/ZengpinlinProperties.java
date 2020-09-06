package com.vailter.standard;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

//@Data
@ConfigurationProperties(prefix = "zengpinlin")
public class ZengpinlinProperties {
    /**
     * 用户名
     */
    private String userName = "test";
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户手机号码
     */
    private Integer phoneNumber;
    /**
     * 上下文属性
     */
    private Map<String, Integer> contexts;
    /**
     * 用户年龄
     */
    private Integer age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Integer> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, Integer> contexts) {
        this.contexts = contexts;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

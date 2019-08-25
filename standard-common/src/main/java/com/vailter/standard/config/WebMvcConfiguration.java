package com.vailter.standard.config;

import com.vailter.standard.config.interceptors.LoginRequiredInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author Vailter67
 * <p>
 * 在spring boot 2.x已经改为最低支持jdk8版本，而jdk8中的接口允许有默认实现，
 * 所以已经废弃掉WebMvcConfigurerAdapter适配类，而改为直接实现WebMvcConfigurer接口
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add interceptors");
        registry.addInterceptor(new LoginRequiredInterceptor()).excludePathPatterns(Arrays.asList("/META-INF/resources/**", "/static/**"));
    }

    /**
     * 自定义静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源处理
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/myprofix/**").addResourceLocations("classpath:/mysource/");
//        registry.addResourceHandler("/myprofix/**").addResourceLocations("file:E:/my/");
    }
}

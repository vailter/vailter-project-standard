package com.vailter.standard.rule.urule.config;

import com.bstek.urule.console.servlet.URuleServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;

/**
 * urule
 */
@Component
public class URuleServletRegistration {
    @Bean
    public ServletRegistrationBean<HttpServlet> registerURuleServlet() {
        return new ServletRegistrationBean<>(new URuleServlet(), "/urule/*");
    }
}

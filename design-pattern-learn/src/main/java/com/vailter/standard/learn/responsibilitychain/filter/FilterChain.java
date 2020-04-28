package com.vailter.standard.learn.responsibilitychain.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface FilterChain {
    public void doFilter(ServletRequest request, ServletResponse response);
}

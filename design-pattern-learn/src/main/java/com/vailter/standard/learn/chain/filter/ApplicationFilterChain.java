package com.vailter.standard.learn.chain.filter;

import org.apache.catalina.core.ApplicationFilterConfig;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ApplicationFilterChain implements FilterChain {
    private final ApplicationFilterConfig[] filters = new ApplicationFilterConfig[0];
    private int pos = 0;//维持过滤器链中的当前位置
    private int n = 0;//过滤器链中的过滤器数量

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) {
        internalDoFilter(request,response);
    }
    private void internalDoFilter(ServletRequest request,
                                  ServletResponse response) {
        //if(pos<n) {
        //    ApplicationFilterConfig filterConfig = filters[pos++];
        //    Filter filter = filterConfig.getFilter();
        //    filter.doFilter(request, response, this);
        //    return;
        //}
    }

    void addFilter(ApplicationFilterConfig filterConfig) {
        //省略了扩容部分。。。
        filters[n++] = filterConfig;
    }
}

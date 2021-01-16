package com.vailter.standard.learn.chain.filter;

import com.vailter.standard.learn.chain.log.Filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //处理request和response
        //...
        chain.doFilter(request, response);
    }
}

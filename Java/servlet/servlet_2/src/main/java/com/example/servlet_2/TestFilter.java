package com.example.servlet_2;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * @author WBS
 * Date:2022/8/24
 */

public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
        System.out.println("初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("执行前。。。");
        chain.doFilter(request, response);   //过滤器是条链
        System.out.println("执行后。。。");
    }

    @Override
    public void destroy() {
//        Filter.super.destroy();
        System.out.println("销毁"); //程序关闭时调用
    }
}

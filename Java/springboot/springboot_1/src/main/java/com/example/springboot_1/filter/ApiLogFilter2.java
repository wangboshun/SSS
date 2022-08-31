package com.example.springboot_1.filter;

import org.apache.catalina.connector.RequestFacade;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author WBS
 * 过滤器2
 * 需要在main函数里面配置扫包
 * Date:2022/8/27
 */

@WebFilter(urlPatterns = "/*",filterName = "ApiLogFilter2")
public class ApiLogFilter2 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        System.out.println("过滤器2=" + ((RequestFacade) request).getRequestURI() + "接口耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

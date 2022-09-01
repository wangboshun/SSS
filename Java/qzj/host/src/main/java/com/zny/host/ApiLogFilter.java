package com.zny.host;

import com.zny.common.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author WBS
 * Date:2022/9/1
 */

@Slf4j
@WebFilter(filterName = "ApiLogFilter", urlPatterns ={"/*"})
public class ApiLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            request.setAttribute("_startTime", System.currentTimeMillis());
            ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, responseWrapper);

            long startTime = (Long) request.getAttribute("_startTime");
            long endTime = System.currentTimeMillis();
            HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
            String url = httpServletRequest.getRequestURI();
            String method = httpServletRequest.getMethod();
            String parameters = httpServletRequest.getQueryString();
            String ip = IpUtils.getRemoteIp(httpServletRequest);
            Integer code = ((HttpServletResponse) response).getStatus();
            String result = new String(responseWrapper.getDataStream());

            response.getOutputStream().write(result.getBytes());

            HashMap<String, Object> map = new HashMap<>();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("code", code);
            map.put("result", result);
            map.put("ip", ip);
            map.put("method", method);
            map.put("parameters", parameters);
            map.put("url", url);

            log.info(map.toString());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

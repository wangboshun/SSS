package com.example.servlet_2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author WBS
 * Date:2022/8/23
 */

public class CookieServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                System.out.println(cookie.getName() + " = " + cookie.getValue());
            }
            Cookie cookie = new Cookie("name", "wbs");
            cookie.setAttribute("value","1234"); //存值
            String value = cookie.getAttribute("value");//取值
            cookie.setMaxAge(30); //过期时间，单位秒
            cookie.setMaxAge(0);//马上过期
            response.addCookie(cookie);

        }
    }
}
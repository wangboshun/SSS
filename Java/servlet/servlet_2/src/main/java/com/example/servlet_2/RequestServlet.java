package com.example.servlet_2;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Enumeration;
import java.util.Map;

/**
 * @author WBS
 * Date:2022/8/23
 */

public class RequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Enumeration<String> names = req.getParameterNames();
        Map<String, String[]> map = req.getParameterMap();
    }
}

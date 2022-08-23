package com.example.servlet_2;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WBS
 * Date:2022/8/23
 */

@WebServlet(value = "/attribute")
public class AttributeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ServletContext context = this.getServletContext();
//        初始化参数
        String name1 = context.getInitParameter("name");
        String name = (String) context.getAttribute("name");
        PrintWriter out = response.getWriter();
        out.println("<h1>" + name + "</h1>");
    }
}

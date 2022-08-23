package com.example.servlet_1;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<h1>Hello</h1>");

        ServletContext context = this.getServletContext();
        context.setAttribute("name", "WBS");

//        请求转发
//        RequestDispatcher dispatcher = context.getRequestDispatcher("/hi");
//        dispatcher.forward(request, response);

        //重定向
        response.sendRedirect("/hi");
//        重定向和转发的区别
//                共同点
//                1.都是跳转到指定的页面
//            不同点
//                1.状态码不一样,一个是302,一个是200
//                2.重定向会更改url
//                3.转发不会更改url
//                4.重定向有两次请求,转发只有一次请求

    }
}
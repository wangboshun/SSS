package com.example.servlet_2;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");


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
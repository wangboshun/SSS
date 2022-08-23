package com.example.servlet_2;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WBS
 * Date:2022/8/22
 */

public class HiServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        ServletContext context = this.getServletContext();
        String name = (String) context.getAttribute("name");

        PrintWriter out = response.getWriter();
        out.println("<h1>" + name + "</h1>");
    }
}

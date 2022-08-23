package com.example.servlet_2;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author WBS
 * Date:2022/8/22
 */

//读取配置文件
public class PropertiesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream stream = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
        Properties properties = new Properties();
        properties.load(stream);
        String name = properties.getProperty("name");
        String password = properties.getProperty("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>" + name + "</h1>");
        out.println("<h1>" + password + "</h1>");
    }
}

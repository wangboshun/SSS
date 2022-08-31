package com.example.servlet_2;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * @author WBS
 * Date:2022/8/23
 */

public class SessionServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        session.setAttribute("name", "heimalang");
//        String name = (String) session.getAttribute("name");
//        session.isNew();//是否刚刚创建
//        session.invalidate();//是否已存在
//        session.removeAttribute("name"); //删除
    }

}

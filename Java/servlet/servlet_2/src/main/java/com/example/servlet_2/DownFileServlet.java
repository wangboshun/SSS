package com.example.servlet_2;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author WBS
 * Date:2022/8/22
 */

public class DownFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取下载文件的路径
        String path = this.getServletContext().getRealPath("")+"/WEB-INF/classes/1.jpg";

        //获取文件名称
        String filename="1.jpg";

        //设置响应头
        response.setHeader("Content-Disposition","attachment;filename="+filename);

        //获取文件输入流
        FileInputStream stream = new FileInputStream(path);

        //创建缓冲区
        int len=0;
        byte[] buffer = new byte[1024];

        //获取响应输出流
        ServletOutputStream outputStream = response.getOutputStream();

        //写入输出流
        while ((len = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        outputStream.close();

        stream.close();
    }
}

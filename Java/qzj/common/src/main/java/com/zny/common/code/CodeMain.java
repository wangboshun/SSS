package com.zny.common.code;

import cn.hutool.core.io.FileUtil;
import com.zny.common.utils.StringUtils;

import java.io.*;
import java.util.Scanner;

/**
 * @author WBS
 * Date:2022/9/7
 */

public class CodeMain {
    public static String className = "";
    public static String moduleName = "";

    public static void main(String[] args) {
        String currentPath = String.valueOf(CodeMain.class.getResource("/")).replace("file:/", "");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入模块名：");
        moduleName = scanner.next();

        System.out.println("请输入类名：");
        className = scanner.next();

        //创建文件夹
        FileUtil.mkdir(currentPath + "generate/");

        //清空文件夹
        FileUtil.clean(currentPath + "generate/");

        generateFile(currentPath + "template/TemplateModel.txt", currentPath + "generate/" + className + "Model.java");
        generateFile(currentPath + "template/TemplateMapper.txt", currentPath + "generate/" + className + "Mapper.java");
        generateFile(currentPath + "template/TemplateApplication.txt", currentPath + "generate/" + className + "Application.java");
        generateFile(currentPath + "template/TemplateController.txt", currentPath + "generate/" + className + "Controller.java");
    }

    public static void generateFile(String txtPath, String javaPath) {
        File txtFile = new File(txtPath);
        File javaFile = new File(javaPath);

        try {
            FileWriter fw = new FileWriter(javaFile);
            BufferedWriter bw = new BufferedWriter(fw);

            FileReader fr = new FileReader(txtFile);
            BufferedReader br = new BufferedReader(fr);

            String s = null;
            while ((s = br.readLine()) != null) {
                className = StringUtils.lowerLineToHump(className);
                s = s.replace("template", className);

                className = StringUtils.capitalizeTheFirstLetter(className);
                s = s.replace("Template", className);

                s = s.replace("模板", moduleName);

                bw.write(s);
                bw.newLine();
            }
            br.close();
            fr.close();

            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

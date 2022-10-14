package com.zny.common.utils;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author WBS
 * Date:2022/9/8
 * 反射帮助类
 */

public class ReflectUtils {

    /**
     * 获取方法上的注释
     *
     * @param className  类名
     * @param methodName 方法名
     */
    public static String getMethodDoc(String className, String methodName) {
        ClassJavadoc classDoc = RuntimeJavadoc.getJavadoc(className);
        for (MethodJavadoc methodDoc : classDoc.getMethods()) {
            if (methodDoc.getName().equals(methodName)) {
                return new CommentFormatter().format(methodDoc.getComment());
            }
        }
        return "";
    }

    /**
     * 获取方法上的注释
     *
     * @param pjp 环绕通知
     */
    public static String getMethodDoc(ProceedingJoinPoint pjp) {
        String methodName = pjp.getSignature().getName();
        Class<?> classTarget = pjp.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        return getMethodDoc(classTarget.getName(), methodName);
    }

    /**
     * 获取接口上的URL
     *
     * @param pjp 环绕通知
     */
    public static String getApiUrl(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        String url = "";
        String methodName = pjp.getSignature().getName();
        Class<?> classTarget = pjp.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        RequestMapping rootAnnotation = AnnotationUtils.findAnnotation(classTarget, RequestMapping.class);

        //是否有区域
        if (rootAnnotation != null) {
            url = Arrays.toString(rootAnnotation.value());
        }

        Method method = classTarget.getMethod(methodName, par);
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);

        //是否有RequestMapping注解
        if (requestMapping != null) {
            url += Arrays.toString(requestMapping.value());
        } else {
            PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);

            //是否有PostMapping注解
            if (postMapping != null) {
                url += Arrays.toString(postMapping.value());
            } else {
                GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);

                //是否有GetMapping注解
                if (getMapping != null) {
                    url += Arrays.toString(getMapping.value());
                }
            }
        }
        return url.replace("[", "").replace("]", "");
    }
}

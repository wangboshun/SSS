package com.wbs.common.extend;

import com.wbs.common.enums.HttpEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WBS
 * @date 2023/3/9 9:38
 * @desciption GlobalExceptionHandler
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(e.getMessage(), HttpEnum.EXCEPTION);
    }
}

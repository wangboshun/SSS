package com.wbs.common.extend;

import com.wbs.common.enums.HttpEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author WBS
 * @date 2023/3/9 9:38
 * @desciption GlobalExceptionHandler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(e.getMessage(), HttpEnum.EXCEPTION);
    }

    /**
     * 处理 form data方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult bindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(message, HttpEnum.EXCEPTION);
    }

    /**
     * 处理Get请求中 验证路径中 单个参数请求失败抛出异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(javax.validation.ConstraintViolation::getMessage).collect(Collectors.joining());
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(message, HttpEnum.EXCEPTION);
    }

    /**
     * 处理 json 请求体调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.insert(0, String.format("%s%s；", fieldError.getField(), fieldError.getDefaultMessage()));
        }
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(message.toString(), HttpEnum.EXCEPTION);
    }

    /**
     * 接口不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseResult error(NoHandlerFoundException e) {
        logger.error("发生异常：" + e.getMessage(), e);
        return new ResponseResult().ERROR(e.getMessage(), HttpEnum.NOT_FOUND);
    }
}

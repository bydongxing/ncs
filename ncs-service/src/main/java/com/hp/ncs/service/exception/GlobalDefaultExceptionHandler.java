package com.hp.ncs.service.exception;


import com.alibaba.fastjson.JSON;
import com.hp.ncs.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常信息
 *
 * @author dongxing
 **/
@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    /**
     * 接口不存在异常
     *
     * @param request
     * @return
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseBody
    public Result noHandlerFoundExceptionHandler(HttpServletRequest request,NoHandlerFoundException e) {
        log.info(e.getMessage());
        return Result.createByErrorCodeMessage(Result.ResponseCode.NOT_FOUND.getCode(), "接口 [" + request.getRequestURI() + "] 不存在");
    }

    /**
     * Precondition failed异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ServletException.class})
    @ResponseBody
    public Result servletExceptionHandler(HttpServletRequest req, ServletException e) {
        log.info(e.getMessage());
        return Result.createByErrorCodeMessage(Result.ResponseCode.PRECONDITION_FAILED.getCode(), e.getMessage());
    }

    /**
     *
     * controller的单个参数注解
     * ConstraintViolationException异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        log.info(e.getMessage());
        return Result.createByErrorCodeMessage(Result.ResponseCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 实现对异常的拦截，当错误在Bean的注解时
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result bindExceptionHandler(HttpServletRequest req, BindException e) {
        log.info(e.getMessage());
        return Result.createByErrorCodeMessage(Result.ResponseCode.BAD_REQUEST.getCode(), JSON.toJSONString(e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList())));
    }

    /**
     * Precondition failed异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Result defaultExceptionHandler(HttpServletRequest req, Exception e,Object handler) {
        String message;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                    req.getRequestURI(),
                    handlerMethod.getBean().getClass().getName(),
                    handlerMethod.getMethod().getName(),
                    e.getMessage());
        } else {
            message = e.getMessage();
        }
        log.error(message, e);
        return Result.createByErrorCodeMessage(Result.ResponseCode.INTERNAL_SERVER_ERROR.getCode(),"接口 [" + req.getRequestURI() + "] 内部错误");
    }

}

package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 公共异常处理类
 *
 * 控制层抛出异常后，都通过BaseExceptionHandler类进行拦截
 * 统一进行处理，返回Result对象
 */
//@ControllerAdvice //控制层通知
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 异常处理方法
     */
    @ExceptionHandler(Exception.class)
    //@ResponseBody
    public Result error(Exception e){
        String message = e.getMessage();
        //根据不同的异常返回相应的错误信息
        if(e instanceof EmptyResultDataAccessException){
            message = "数据为空";
        }

        System.out.println("报异常了。。。。。"+e.getMessage());
        return new Result(false, StatusCode.ERROR,message);
    }
}

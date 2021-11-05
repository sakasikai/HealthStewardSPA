package com.healthsteward.controller.exhandlers;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExController {
    private static final Logger logger = LoggerFactory.getLogger(CustomExController.class);


    // 捕捉shiro的异常
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED,
                    reason = "身份认证失败")
    public String authHandler(Throwable ex) {
        logger.info("身份认证失败："+ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST,
            reason = "请求参数有误")
    public String illegalArgumentHandler(Throwable ex){
        logger.info("Global IllegalArgumentException Handler:"+ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED,
            reason = "权限不足")
    public String permissionHandler(Throwable ex){
        logger.info("Global IllegalArgumentException Handler:"+ex.getMessage());
        return ex.getMessage();
    }

}

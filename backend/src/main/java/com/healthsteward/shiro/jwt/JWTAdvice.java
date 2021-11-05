package com.healthsteward.shiro.jwt;


import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class JWTAdvice {
    private static final Logger logger = LoggerFactory.getLogger(JWTAdvice.class);

    @Pointcut("execution(* com.homecare.controller.healthsteward..*.*(String,..)) && args(userName,..)" +
            "||execution(* com.homecare.controller.shoppingmall..*.*(String,..)) && args(userName,..)" )
    private void validateUserName(String userName) {
        // 该函数用作Pointcut标识符, 匹配controller.healthSteward下所有函数，
        // 限制条件为：controller.method 的第一个参数必须是String, 且代表着访问API的userName
        // 注意：参数userName代表着advice织入方法的第一个参数，只跟参数位置有关！
        // +"||execution(* com.homecare.controller.user.StaffController.*(String,..)) && args(userName,..)"
    }



    @Before(value = "validateUserName(userName)", argNames = "userName")
    public void jwtPostValidate(String userName) throws AuthenticationException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

        String Authorization = request.getHeader("Authorization");
        if(Authorization == null)
            throw new AuthenticationException("请求需要token!");
        String token = Authorization.substring("bearer ".length());

        if(userName.equals(JWTUtil.getUserName(token))){
            return;
        }else{
            throw new AuthenticationException("请求的token和访问身份冲突!");
        }
    }

}

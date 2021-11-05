package com.healthsteward.filter;

import com.healthsteward.shiro.CustomToken;
import com.healthsteward.util.ZQTemp.AuthenticationType;
import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 把jwt整合到shiro的 authentication 机制中
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private boolean exPatch=false; //每触发异常，executeLogin 会跑两次，这个patch用于阻止第二次操作！

   @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {

        if (isLoginAttempt(request, response)) { //判断请求的请求头是否带上 "Token"
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                responseError(request, response, e.getMessage());
                exPatch=true;
                return false;
            }
        }

        logger.info("请求不受限制，一律放行"); //请求头不存在 Token,说明是游客模式，因为前端做了router authentication
        return true;
    }

    /**
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        return token != null;
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        if(exPatch) {
            exPatch = false; //重置
            return false;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring("bearer ".length());

        CustomToken jwtToken = new CustomToken(
                (String) JWTUtil.getUserName(token),
                token,
                AuthenticationType.JWT);

        // 提交给realm进行登入
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /filterEx/jwtAuthFail 统一处理
     */
    private void responseError(ServletRequest request, ServletResponse response, String message) throws AuthenticationException{

        try {
            request.setAttribute("msg", message);
            request.getRequestDispatcher("/filterEx/jwtAuthFail").forward(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ServletException e) {
            logger.info(e.getMessage());
        } catch (AuthenticationException e){
            logger.info(e.getMessage());
        }
    }
}

package com.healthsteward.controller.exhandlers;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/filterEx")
@Controller
public class FilterExController {
    @RequestMapping("/jwtAuthFail")
    public void jwtAuthExHandler(HttpServletRequest request) {
        throw new AuthenticationException((String) request.getAttribute("msg"));
    }

    @RequestMapping("/permissionFail")
    public void permExHandler(HttpServletRequest request) {
        throw new UnauthorizedException((String) request.getAttribute("msg"));
    }
}

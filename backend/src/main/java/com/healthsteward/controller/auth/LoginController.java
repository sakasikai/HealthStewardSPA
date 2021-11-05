package com.healthsteward.controller.auth;


import com.healthsteward.pojo.DTO.auth.LoginDTO;
import com.healthsteward.service.interfaces.user.ClientService;
import com.healthsteward.shiro.CustomToken;
import com.healthsteward.util.ZQTemp.AuthenticationType;
import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
public class LoginController {

    private final ClientService clientService;

    @Autowired
    public LoginController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(path = "/login", method= RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginDTO model){
        if(model.getUserName()==null||model.getPassword()==null){
            throw new AuthenticationException("用户名或密码为空");
        }
        CustomToken token = new CustomToken(
                model.getUserName(),
                model.getPassword(),
                AuthenticationType.UsernamePassword);
        SecurityUtils.getSubject().login(token);
        return new ResponseEntity<>(JWTUtil.createToken(model.getUserName()), HttpStatus.OK);
    }

}

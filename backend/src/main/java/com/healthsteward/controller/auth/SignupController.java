package com.healthsteward.controller.auth;

import com.healthsteward.pojo.DTO.auth.SignupDTO;
import com.healthsteward.service.interfaces.user.StaffService;
import com.healthsteward.service.interfaces.auth.AuthService;
import com.healthsteward.service.interfaces.auth.UserService;
import com.healthsteward.service.interfaces.user.ClientService;
import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class SignupController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthService authService;
    private final UserService userService;
    private final ClientService clientService;
    private final StaffService staffService;

    @Autowired
    public SignupController(AuthService authService, UserService userService, ClientService clientService, StaffService staffService) {
        this.authService = authService;
        this.userService = userService;
        this.clientService = clientService;
        this.staffService = staffService;
    }


    @RequestMapping(path = "/signup", method= RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody SignupDTO model){
        if(authService.checkUserExistence(model.getUserName())){
            throw new AuthenticationException("用户名已存在，请重新注册");
        }

        int userId = userService.registerUser(model.getUserName());
        authService.saveAuth(model, userId);
        clientService.initClient(model, userId);

        // set response headers
        HttpHeaders headers = new HttpHeaders();
        // 这里的url暂时保留，以后再研究
        URI locationUri = URI.create("http://health_steward/client/"+model.getUserName());
        headers.setLocation(locationUri);

        return new ResponseEntity<>(JWTUtil.createToken(model.getUserName()), headers, HttpStatus.CREATED);
    }

    //直接员工注册,不设置staff角色，不需要有role，user_role的表
    @PostMapping("/signup/staff")
    public ResponseEntity<String> staffSignup(@RequestBody SignupDTO model){
        if(authService.checkUserExistence(model.getUserName())) {
            throw new AuthenticationException("用户名已存在，请重新注册");
        }

        int userId = userService.registerUser(model.getUserName());
        authService.saveAuth(model, userId);
        staffService.saveStaff(userId, model.getUserName(), model.getNickName());

        // set response headers
        HttpHeaders headers = new HttpHeaders();
        // 这里的url暂时保留，以后再研究
        URI locationUri = URI.create("http://health_steward/staff/"+model.getUserName());
        headers.setLocation(locationUri);

        return new ResponseEntity<>(JWTUtil.createToken(model.getUserName()), headers, HttpStatus.CREATED);

    }

    @GetMapping("/checkExists/{username}")
    public ResponseEntity<String> checkUsernameExists(@PathVariable String username){
        boolean b = userService.checkUserNameExists(username);
        if(b)
            return new ResponseEntity<>("", HttpStatus.CONFLICT);
        else
            return ResponseEntity.ok("");
    }
}

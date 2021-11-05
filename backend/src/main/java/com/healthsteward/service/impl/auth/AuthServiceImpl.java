package com.healthsteward.service.impl.auth;

import com.healthsteward.mapper.auth.AuthMapper;
import com.healthsteward.pojo.DO.auth.Authentication;
import com.healthsteward.pojo.DTO.auth.SignupDTO;
import com.healthsteward.service.interfaces.auth.AuthService;
import com.healthsteward.util.ZQTemp.RandomStrUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AuthenticationService")
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;

    @Autowired
    public AuthServiceImpl(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }



    @Override
    public void saveAuth(SignupDTO obj, int id) {
        // 随机生成salt，并生成passwordInDB，存储
        Authentication auth = new Authentication();
        auth.setUserName(obj.getUserName());
        auth.setSalt(RandomStrUtil.generateString(10));
        auth.setPasswordInDB(new Md5Hash(obj.getPassword(), auth.getSalt(), 2).toString()); // 此处加密算法要和shiro身份认证里的算法一致！！！
        auth.setUserId(id);
        authMapper.insertAuth(auth);
    }

    public boolean checkUserExistence(String userName){
        // Auth里字段不空,则用户存在
        Authentication auth = authMapper.getAuthByName(userName);
        if(auth!=null && auth.getPasswordInDB()!=null && auth.getSalt()!=null){
            return true;
        }
        return false;
    };

    @Override
    public Authentication getAuthByUserName(String userName) {
        return authMapper.getAuthByName(userName);
    }

    @Override
    public boolean checkPassword(String username, String password) {
        Authentication auth = this.getAuthByUserName(username);
        String passInput = new Md5Hash(password, auth.getSalt(), 2).toString();
        System.out.println("input:"+passInput);
        System.out.println("db:"+auth.getPasswordInDB());
        return passInput.equals(auth.getPasswordInDB());
    }

    @Override
    public boolean updateAuth(String username, String password) {
        Authentication auth = new Authentication();
        auth.setUserName(username);
        auth.setSalt(RandomStrUtil.generateString(10));
        auth.setPasswordInDB(new Md5Hash(password, auth.getSalt(), 2).toString());
        return authMapper.updateAuth(auth);
    }
}

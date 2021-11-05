package com.healthsteward.service.impl.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.service.interfaces.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int registerUser(String userName) {
        userMapper.insertUser(userName);
        return userMapper.getIdByName(userName);
    }

    @Override
    public boolean checkUserNameExists(String username) {
        return userMapper.countIdByName(username)>0;
    }
}

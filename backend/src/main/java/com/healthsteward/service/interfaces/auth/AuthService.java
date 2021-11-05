package com.healthsteward.service.interfaces.auth;

import com.healthsteward.pojo.DO.auth.Authentication;
import com.healthsteward.pojo.DTO.auth.SignupDTO;


public interface AuthService {
    // auth
    void saveAuth(SignupDTO obj, int id);
    boolean checkUserExistence(String userName);

    //
    Authentication getAuthByUserName(String userName);

    boolean checkPassword(String username, String password);
    boolean updateAuth(String username, String password);
}

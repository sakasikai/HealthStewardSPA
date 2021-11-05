package com.healthsteward.service.interfaces.auth;

public interface UserService {
    public int registerUser(String userName);
    public boolean checkUserNameExists(String username);
}

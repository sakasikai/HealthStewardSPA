package com.healthsteward.mapper.auth;

import com.healthsteward.pojo.DO.auth.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthMapper {
    // Authentication
    boolean insertAuth(Authentication auth);
    boolean deleteAuth(String userName);
    boolean updateAuth(Authentication auth);
    Authentication getAuthByName(String userName);
}

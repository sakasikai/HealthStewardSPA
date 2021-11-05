package com.healthsteward.mapper.auth;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    public void insertUser(String userName);
    public int getIdByName(String userName);
    public String getNameById(Integer userId);
    public int countIdByName(String userName);
}

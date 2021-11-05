package com.healthsteward.pojo.DO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {
    String userName;
    String passwordInDB;
    String salt; // 注册时随机生成字符串，长6位
    Integer userId;
}

package com.healthsteward.pojo.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDTO {
    private String userName;
    private String password;
    private String nickName;
}

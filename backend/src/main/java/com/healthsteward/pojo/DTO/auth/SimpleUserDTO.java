package com.healthsteward.pojo.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SimpleUserDTO {
    private Integer userId;
    private String userName;
    private String nickName;
    private Integer age;
    private Integer gender;
}

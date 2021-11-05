package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleStaffDTO {
    private Integer userId;
    private String userName;
    private String nickName;
    private Integer gender;
    private String job;//职务
    private String post;//职称
    private String phone;
    private String resume;//简介
}

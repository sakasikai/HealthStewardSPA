package com.healthsteward.pojo.DO.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.pojo.DTO.user.StaffDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("staff")
public class Staff {
    private Integer userId;
    @TableId
    private String username;
    private String nickname;
    private Integer age;
    private Integer gender;
    private String type;//员工类型
    private Date hireDate;
    private String job;//职务
    private String post;//职称
    private String phone;
    private String employeeId;
    private String resume;//简介
    private String remark;//备注
    private Object image;

    public StaffDTO toStaffDTO(){
        StaffDTO staffDTO = new StaffDTO();
        BeanUtils.copyProperties(this,staffDTO);
        return staffDTO;
    }

    public SimpleUserDTO toSimpleUserDTO(){
        SimpleUserDTO obj = new SimpleUserDTO(
                this.userId,
                this.username,
                this.nickname,
                this.age,
                this.gender);
        return obj;
    }
}

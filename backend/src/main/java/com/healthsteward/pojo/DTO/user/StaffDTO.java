package com.healthsteward.pojo.DTO.user;

import com.healthsteward.pojo.DO.user.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO {
    private Integer userId;
    private String username;
    private String nickname;
    private Integer age;
    private Integer gender;
    private String type;
    private Date hireDate;
    private String job;
    private String post;//职称
    private String phone;
    private String employeeId;
    private String resume;
    private String remark;

    public Staff toStaff(){
        Staff staff = new Staff();
        BeanUtils.copyProperties(this,staff);
        return staff;
    }
}

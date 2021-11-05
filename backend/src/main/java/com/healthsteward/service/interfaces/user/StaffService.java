package com.healthsteward.service.interfaces.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.healthsteward.pojo.DO.user.Staff;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;

public interface StaffService extends IService<Staff> {
    boolean saveStaff(Staff staff);
    boolean saveStaff(Integer userId, String username,String nickName);
    String getStaffNameByUsername(String username);
    Staff getStaffByUserName(String username);
    boolean updateStaff(Staff staff);
    boolean removeStaff(Integer userId);
    boolean removeStaff(String username);


    Object getStaffImage(String username);
    boolean updateStaffImage(String username, String image);

    SimpleUserDTO getSimpleUserDTOByUserName(String username);
}

package com.healthsteward.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthsteward.mapper.auth.AuthMapper;
import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.mapper.user.StaffMapper;
import com.healthsteward.pojo.DO.user.Staff;
import com.healthsteward.pojo.DO.auth.Authentication;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.service.interfaces.user.StaffService;
import com.healthsteward.util.ZQTemp.RandomStrUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {
    private final AuthMapper authMapper;
    private final UserMapper userMapper;

    @Autowired
    public StaffServiceImpl(AuthMapper authMapper, UserMapper userMapper) {
        this.authMapper = authMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean saveStaff(Staff staff) {
        return this.save(staff);
    }

    @Override
    public boolean saveStaff(Integer userId, String username, String nickname) {
        Staff staff = new Staff();
        staff.setUserId(userId);
        staff.setUsername(username);
        staff.setNickname(nickname);
        return this.save(staff);
    }

    @Override
    public String getStaffNameByUsername(String username) {
        return this.getOne(new QueryWrapper<Staff>().eq("username",username)).getNickname();
    }

    @Override
    public Staff getStaffByUserName(String username) {
        return this.getOne(new QueryWrapper<Staff>().eq("username",username));
    }

    @Override
    public boolean updateStaff(Staff staff) {
//        return this.update(staff,new UpdateWrapper<Staff>().eq("username",staff.getUsername()));
        return this.updateById(staff);
    }

    @Override
    public boolean removeStaff(Integer userId) {
        return this.removeById(userId);
    }

    @Override
    public boolean removeStaff(String username) {
        return this.remove(new QueryWrapper<Staff>().eq("username",username));
    }


    @Override
    public Object getStaffImage(String username) {
        Staff staff = this.getStaffByUserName(username);
        if(staff == null)
            return null;
        return staff.getImage();
    }

    @Override
    public boolean updateStaffImage(String username, String image) {
        return this.update(new UpdateWrapper<Staff>().eq("username",username).set("image",image));
    }

    @Override
    public SimpleUserDTO getSimpleUserDTOByUserName(String username) {
        Staff staff = this.getStaffByUserName(username);
        return new SimpleUserDTO(
                staff.getUserId(),
                staff.getUsername(),
                staff.getNickname(),
                staff.getAge(),
                staff.getGender()
        );
    }
}

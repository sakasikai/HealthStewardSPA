package com.healthsteward.pojo.DO.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.pojo.DTO.user.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("client")
public class Client {
    private Integer userId;
    @TableId
    private String username;
    private String nickname;
    private Integer age;
    private Integer gender;
    private String idNumber;
    private String type;
    private Integer fileNumber;
    private String phone;
    private Date birthday;
    private Double height;
    private Double weight;
    private String bloodType;
    private Object image;
    private String remark;

    public ClientDTO toClientDTO(){
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(this,clientDTO);
        return clientDTO;
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

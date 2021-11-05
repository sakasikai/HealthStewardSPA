package com.healthsteward.pojo.DTO.user;

import com.healthsteward.pojo.DO.user.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private String username;
    private String nickname;
    private Integer age;
    private Integer gender;
    private String idNumber;
    private String type;
    private Integer fileNumber;
    private Date phone;
    private Date birthday;
    private Double height;
    private Double weight;
    private String bloodType;
    private String remark;

    public Client toClient(){
        Client client = new Client();
        BeanUtils.copyProperties(this,client);
        return client;
    }
}

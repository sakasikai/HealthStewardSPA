package com.healthsteward.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthsteward.pojo.DO.user.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientMapper extends BaseMapper<Client> {
    boolean insertClient(Client obj);
    Client getClientById(Integer clientId);
    String getNickNameById(Integer clientId);
    Boolean updateImageById(Client obj);
}

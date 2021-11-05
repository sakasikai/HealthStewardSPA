package com.healthsteward.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.mapper.user.ClientMapper;
import com.healthsteward.pojo.DO.user.Client;
import com.healthsteward.pojo.DO.user.Staff;
import com.healthsteward.pojo.DTO.auth.SignupDTO;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.service.interfaces.user.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Autowired
    public ClientServiceImpl(ClientMapper clientMapper, UserMapper userMapper) {
        this.clientMapper = clientMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean initClient(SignupDTO obj, int id) {
        Client client = new Client();
        client.setUserId(id);
        client.setUsername(obj.getUserName());
        client.setNickname(obj.getNickName());
        return clientMapper.insertClient(client);
    }

    @Override
    public SimpleUserDTO getSimpleUserDTOByUserName(String userName) {
        int userId = userMapper.getIdByName(userName);
        Client client = clientMapper.getClientById(userId);
        return new SimpleUserDTO(
                userId,
                client.getUsername(),
                client.getNickname(),
                client.getAge(),
                client.getGender()
        );
    }

    @Override
    public boolean saveClient(Client client) {
        return clientMapper.insert(client) > 0;
    }

    @Override
    public boolean updateClient(Client client) {
        if(client.getUserId() != null)
            return clientMapper.updateById(client) > 0;
        else
            return clientMapper.update(client,new QueryWrapper<Client>().eq("username",client.getUsername())) > 0;
    }

    @Override
    public Client getClient(String username) {
        return clientMapper.selectOne(new QueryWrapper<Client>().eq("username",username));
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        return userMapper.getIdByName(username);
    }

    @Override
    public String getUsernameByUserId(Integer userId) {
        return userMapper.getNameById(userId);
    }

    @Override
    public boolean updateClientImageByUserId(Integer clientId, String image) {
        Client client = new Client();
        client.setUserId(clientId);
        client.setImage(image);
//        return clientMapper.updateById(client) > 0;
        return clientMapper.updateImageById(client);
    }

    @Override
    public Object getClientImageByUserId(Integer clientId) {
        return clientMapper.getClientById(clientId).getImage();
    }
}

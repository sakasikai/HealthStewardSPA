package com.healthsteward.service.interfaces.user;

import com.healthsteward.pojo.DO.user.Client;
import com.healthsteward.pojo.DTO.auth.SignupDTO;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;

public interface ClientService {
    // client info
    boolean initClient(SignupDTO obj, int id); // 注册后初始化
    SimpleUserDTO getSimpleUserDTOByUserName(String userName);
    boolean saveClient(Client client);
    boolean updateClient(Client client);
    Client getClient(String username);

    Integer getUserIdByUsername(String username);
    String getUsernameByUserId(Integer userId);

    boolean updateClientImageByUserId(Integer clientId,String image);
    Object getClientImageByUserId(Integer clientId);

    // operation
//    public int getUsedNumber(int clientId, int servId);
//    public int setUsedNumber(int clientId, int servId, int num);
//
//    public int getTotalNumber(int clientId, int servId);
//    public int setTotalNumber(int clientId, int servId, int num);
//
//    public Date getStarMarkedTime(int clientId, int servId);
//    public boolean setStar(int clientId, int servId, Date timeStamp);
//
//    public Date getHistoryMarkedTime(int clientId, int servId);
//    public boolean setHistory(int clientId, int servId, Date timeStamp);
}

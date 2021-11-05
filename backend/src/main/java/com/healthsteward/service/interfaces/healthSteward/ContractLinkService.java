package com.healthsteward.service.interfaces.healthSteward;

import com.healthsteward.pojo.DTO.healthsteward.ContractInfoDTO;
import com.healthsteward.pojo.DTO.healthsteward.ContractItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SignedItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SimpleStaffDTO;

import java.util.Date;
import java.util.List;

public interface ContractLinkService {

    // talkLink ：查看此client是否已经被预约洽谈
    public boolean checkTalkLink(String clientName);

    // contract life circle
    public void initContractLink(String clientName, String staffName, Date timeStamp);
    public void removeContractLink(String clientName, String staffName);

    // Contract Confirm (client & staff)
    public int getClientConfirmed(String clientName, String staffName);
    // 返回值：true 代表了不要触发备选事件流,正常执行set功能
    public boolean setClientConfirm(String clientName, String staffName, int option);

    public int getStaffConfirmed(String clientName, String staffName);
    public boolean setStaffConfirm(String clientName, String staffName, int option);

    // contract info
    public String getSignedTime(String clientName);

    public int getContractState(String clientName, String staffName);

    // staff note
//    public String getStaffNote(String clientName, String staffName);
//    public boolean setStaffNote(String clientName, String staffName, String note);


    // 为 contractState 的 failed state 添加说明
    public ContractInfoDTO getContractInfo(String clientName, String staffName);

    // staff确认合同签约成功后，将contractLink转移到管理洽谈列表
    public void setContractManaged(String clientName, String staffName);

    // 管理已签约用户
    public List<SignedItemDTO> listValidSignedClients(String staffName);

    // 管理洽谈列表
    public List<ContractItemDTO> listContractItemDTOs(String staffName);

    // staff info
    public SimpleStaffDTO getStaffInfo(String userName);
}

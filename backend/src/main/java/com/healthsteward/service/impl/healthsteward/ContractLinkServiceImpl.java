package com.healthsteward.service.impl.healthsteward;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.healthsteward.mapper.user.StaffMapper;
import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.mapper.healthsteward.ContractLinkMapper;
import com.healthsteward.mapper.user.ClientMapper;
import com.healthsteward.pojo.DO.user.Staff;
import com.healthsteward.pojo.DO.healthsteward.ContractLink;
import com.healthsteward.pojo.DTO.healthsteward.ContractInfoDTO;
import com.healthsteward.pojo.DTO.healthsteward.ContractItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SignedItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SimpleStaffDTO;
import com.healthsteward.service.interfaces.healthSteward.ContractLinkService;
import com.healthsteward.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContractLinkServiceImpl implements ContractLinkService {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ContractLinkMapper contractLinkMapper;
    private final StaffMapper staffMapper;


    @Autowired
    public ContractLinkServiceImpl(UserMapper userMapper, ClientMapper clientMapper, ContractLinkMapper contractLinkMapper, StaffMapper staffMapper) {
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.contractLinkMapper = contractLinkMapper;
        this.staffMapper = staffMapper;
    }

    @Override
    public boolean checkTalkLink(String clientName) {
        int userId = userMapper.getIdByName(clientName);
        ContractLink tmp = contractLinkMapper.getContractLink(userId, null);
        return tmp!=null;
    }


    @Override
    public void initContractLink(String clientName, String staffName, Date timeStamp) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        contractLinkMapper.insertContractLink(clientId, staffId, timeStamp);
    }

    @Override
    public void removeContractLink(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        contractLinkMapper.deleteContractLink(clientId, staffId);
    }

    @Override
    public int getClientConfirmed(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        ContractLink contract =  contractLinkMapper.getContractLink(clientId, staffId);
        return contract.getClientConfirm();
    }

    @Override
    public boolean setClientConfirm(String clientName, String staffName, int option) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        // ?????? option ??????????????????
        // ??????contract_state??????????????????????????????result_time_stamp,result_comment
        ContractLink contractLink = contractLinkMapper.getContractLink(clientId,staffId);
        int staffConfirm = contractLink.getStaffConfirm();
        if(staffConfirm==0){
            // staff??????????????????????????????setStaffConfirm controller????????????????????????????????????
            contractLinkMapper.updateClientConfirm(clientId, staffId, option);
            return true;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId",clientId);
        params.put("staffId",staffId);
        params.put("clientConfirm",option);

        boolean res =true;
        switch (option){
            case 1:
                if(staffConfirm==1){
                    // ????????????
                    params.put("contractState", 1);
                    params.put("resultComment", "????????????");
                    params.put("resultTimeStamp", new Date());
                }
                // when staffConfirm == -1 do nothing
                break;
            case 0:
                // ???????????????
                params.put("contractState", 0);
                params.put("resultComment", clientName+"??????????????????");
                params.put("resultTimeStamp", new Date());
                params.put("managedState", -2);
                res = false;
                break;
            default:
                break;
        }

        contractLinkMapper.updateContractLinkByMap(params);
        return res;
    }

    @Override
    public int getStaffConfirmed(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        ContractLink contract =  contractLinkMapper.getContractLink(clientId, staffId);
        return contract.getStaffConfirm();
    }

    @Override
    public boolean setStaffConfirm(String clientName, String staffName, int option) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);

        // ?????? option ??????????????????
        // ??????contract_state??????????????????????????????result_time_stamp,result_comment
        ContractLink contractLink = contractLinkMapper.getContractLink(clientId,staffId);
        int clientConfirm = contractLink.getClientConfirm();
        if(clientConfirm==0){
            // client??????????????????????????????setClientConfirm controller????????????????????????????????????
            contractLinkMapper.updateStaffConfirm(clientId, staffId, option);
            return true;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId",clientId);
        params.put("staffId",staffId);
        params.put("staffConfirm",option);

        boolean res =true;
        switch (option){
            case 1:
                if(clientConfirm==1){
                    // ????????????
                    params.put("contractState", 1);
                    params.put("resultComment", "????????????");
                    params.put("resultTimeStamp", new Date());
                }
                // when clientConfirm == -1 do nothing
                break;
            case 0:
                // ????????????????????????, ??????managedState?????????
                params.put("contractState", 0);
                params.put("resultComment", staffName+"??????????????????");
                params.put("resultTimeStamp", new Date());
                params.put("managedState", -1);
                res = false;
                break;
            default: // case -1 staff????????????client???????????????, ?????????result
                break;
        }
        contractLinkMapper.updateContractLinkByMap(params);
        return res;
    }

    @Override
    public String getSignedTime(String clientName) {
        int clientId = userMapper.getIdByName(clientName);
        ContractLink contract =  contractLinkMapper.getContractLink(clientId, null);
        return DateFormat.format(contract.getResultTimeStamp(), "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public int getContractState(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        ContractLink contract =  contractLinkMapper.getContractLink(clientId, staffId);
        return contract.getContractState();
    }

    @Override
    public ContractInfoDTO getContractInfo(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        ContractLink contract =  contractLinkMapper.getContractLink(clientId, staffId);
        ContractInfoDTO dto = new ContractInfoDTO(
                staffName,
                clientName,
                contract.getContractState(),
                contract.getCreationTimeStamp(),
                contract.getResultTimeStamp(),
                contract.getResultComment());
        return dto;
    }

    @Override
    public void setContractManaged(String clientName, String staffName) {
        int clientId = userMapper.getIdByName(clientName);
        int staffId = userMapper.getIdByName(staffName);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId",clientId);
        params.put("staffId",staffId);
        params.put("staffConfirm",1); // ?????????????????????????????????sql SET?????????????????????

        params.put("resultComment", clientName+"?????? ??? "+staffName+"?????? ?????????????????????");
        params.put("resultTimeStamp", new Date());
        params.put("managedState", 1);

        contractLinkMapper.updateContractLinkByMap(params);
    }

    @Override
    public List<ContractItemDTO> listContractItemDTOs(String staffName) {
        int staffId = userMapper.getIdByName(staffName);
        List<ContractLink> items = contractLinkMapper.listContractLink(staffId);
        List<ContractItemDTO> dtoList = new ArrayList<>();
        for (ContractLink clink: items) {
            ContractItemDTO dto = new ContractItemDTO(
                    clink.getContractId(),
                    userMapper.getNameById(clink.getClientId()),
                    clientMapper.getNickNameById(clink.getClientId()),
                    DateFormat.format(clink.getCreationTimeStamp(), "yyyy-MM-dd HH:mm:ss"),
                    clink.getClientConfirm(),
                    clink.getStaffConfirm(),
                    clink.getContractState()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<SignedItemDTO> listValidSignedClients(String staffName) {
        int staffId = userMapper.getIdByName(staffName);
        List<ContractLink> items = contractLinkMapper.listValidSignedClients(staffId);
        List<SignedItemDTO> dtoList = new ArrayList<>();
        for (ContractLink clink: items) {
            SignedItemDTO dto = new SignedItemDTO(
                    clink.getContractId(),
                    userMapper.getNameById(clink.getClientId()),
                    clientMapper.getNickNameById(clink.getClientId()),
                    DateFormat.format(clink.getCreationTimeStamp(), "yyyy-MM-dd HH:mm:ss")
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public SimpleStaffDTO getStaffInfo(String userName) {
        int userId = userMapper.getIdByName(userName);
        ContractLink contractLink = contractLinkMapper.getContractLink(userId,null);
        Staff staff = staffMapper.selectOne(new QueryWrapper<Staff>().eq("user_id", contractLink.getStaffId()));
        SimpleStaffDTO dto = new SimpleStaffDTO(
                staff.getUserId(),staff.getUsername(),staff.getNickname(),staff.getGender(),
                staff.getJob(),staff.getPost(),staff.getPhone(),staff.getResume());
        return dto;
    }
}

package com.healthsteward.service.impl.healthsteward;

import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.mapper.healthsteward.HealthStewardMapper;
import com.healthsteward.mapper.user.ClientMapper;
import com.healthsteward.pojo.DO.user.Client;
import com.healthsteward.pojo.DO.healthsteward.ApplyItem;
import com.healthsteward.pojo.DTO.healthsteward.ApplyCreationDTO;
import com.healthsteward.pojo.DTO.healthsteward.ApplyItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SimpleStaffDTO;
import com.healthsteward.service.interfaces.healthSteward.HealthStewardService;
import com.healthsteward.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HealthStewardServiceImpl implements HealthStewardService {

    private final HealthStewardMapper healthStewardMapper;
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;

    @Autowired
    public HealthStewardServiceImpl(HealthStewardMapper healthStewardMapper, UserMapper userMapper, ClientMapper clientMapper) {
        this.healthStewardMapper = healthStewardMapper;
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
    }

    @Override
    public int getApplyFlowLevelByName(String userName) {
        int userId = userMapper.getIdByName(userName);
        ApplyItem item = healthStewardMapper.getApplyItemByClientId(userId);
        return item!=null?item.getFlowLevel():0;
    }

    @Override
    public void nextApplyFlowLevelByName(String userName) {
        int userId = userMapper.getIdByName(userName);
        ApplyItem item = healthStewardMapper.getApplyItemByClientId(userId);
        if(item==null){return;}
        healthStewardMapper.updateFlowLevelByClientId(userId, item.getFlowLevel() + 1);
    }

    @Override
    public int saveApplyItem(ApplyCreationDTO dto, String userName) throws ParseException {
        int userId = userMapper.getIdByName(userName);
        Map params = new HashMap<String, Object>();
        params.put("clientId", userId);
        params.put("timeStamp", DateFormat.parse(dto.getTimeStamp(), "yyyy-MM-dd-HH:mm:ss"));
        params.put("applyNote", dto.getApplyNote());
        params.put("flowLevel", dto.getFlowLevel());
        healthStewardMapper.insertApplyItem(params);
        ApplyItem tmp = healthStewardMapper.getApplyItemByClientId(userId);
        return tmp.getApplyItemId();
    }

    @Override
    public void removeApplyItemByName(String userName) {
        int userId = userMapper.getIdByName(userName);
        healthStewardMapper.deleteApplyItemById(userId);
    }

    @Override
    public List<ApplyItemDTO> listApplyItemDTOs() {
        List<ApplyItem> applyItemDOs = healthStewardMapper.listApplyItems(false);
        List<ApplyItemDTO> res = new ArrayList<ApplyItemDTO>();
        for (ApplyItem item: applyItemDOs) {
            Client client = clientMapper.getClientById(item.getClientId());
            res.add(new ApplyItemDTO(
                    item.getApplyItemId(),
                    client.getUsername(),
                    client.getNickname(),
                    DateFormat.format(item.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"),
                    item.getApplyNote(),
                    item.getFlowLevel()));
        }

        return res;
    }


}

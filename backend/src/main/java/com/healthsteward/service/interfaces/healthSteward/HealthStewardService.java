package com.healthsteward.service.interfaces.healthSteward;

import com.healthsteward.pojo.DO.healthsteward.ApplyItem;
import com.healthsteward.pojo.DTO.healthsteward.ApplyCreationDTO;
import com.healthsteward.pojo.DTO.healthsteward.ApplyItemDTO;
import com.healthsteward.pojo.DTO.healthsteward.SimpleStaffDTO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface HealthStewardService {
    // flow level
    public int getApplyFlowLevelByName(String userName);
    public void nextApplyFlowLevelByName(String userName); // flowLevel++

    // apply item
    public int saveApplyItem(ApplyCreationDTO item, String userName) throws ParseException;
    public void removeApplyItemByName(String userName);
    public List<ApplyItemDTO> listApplyItemDTOs();


    // talk

    // contract


}

package com.healthsteward.controller.healthsteward;


import com.healthsteward.pojo.DTO.healthsteward.ApplyCreationDTO;
import com.healthsteward.pojo.DTO.healthsteward.SimpleStaffDTO;
import com.healthsteward.service.interfaces.healthSteward.ContractLinkService;
import com.healthsteward.service.interfaces.healthSteward.HealthStewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.ParseException;

@RestController
@RequestMapping(path = "/{clientName}/HealthSteward")
public class ClientHealthStewardController {

    private final HealthStewardService healthStewardService;
    private final ContractLinkService contractLinkService;

    @Autowired
    public ClientHealthStewardController(HealthStewardService healthStewardService1, ContractLinkService contractLinkService ) {
        this.healthStewardService = healthStewardService1;
        this.contractLinkService = contractLinkService;
    }

    // ApplyItem
    @RequestMapping(path = "/ApplyItem", method=RequestMethod.POST)
    public ResponseEntity saveApplyItem(@PathVariable String clientName, @RequestBody ApplyCreationDTO item) throws ParseException {

        if(contractLinkService.checkTalkLink(clientName)){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        // 创建新的申请项
        int itemId = healthStewardService.saveApplyItem(item, clientName);
        // 每个flow结束后，就要改变level
        healthStewardService.nextApplyFlowLevelByName(clientName);

        // set response headers
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = URI.create("http://xxx/ApplyItem/"+itemId);
        headers.setLocation(locationUri);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    // ApplyFlowLevel
    @RequestMapping(path = "/ApplyFlowLevel", method = RequestMethod.GET)
    public ResponseEntity<Integer> getApplyFlowLevel(@PathVariable String clientName){
        Integer payload = healthStewardService.getApplyFlowLevelByName(clientName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // TalkLink
    @RequestMapping(path = "/TalkLink", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkTalkLink(@PathVariable String clientName){
        // 检查该client是否有医生已经预约了TalkLink, 返回boolean
        Boolean payload = contractLinkService.checkTalkLink(clientName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // ContractLink
    @RequestMapping(path = "/{staffName}/ClientContractConfirm", method = RequestMethod.PUT)
    public ResponseEntity setClientContractConfirm(@PathVariable String clientName,
                                                   @PathVariable String staffName,
                                                   @RequestParam int num){

        if(contractLinkService.setClientConfirm(clientName,staffName, num)){
            // 每个flow正常结束后，都要改变level
            healthStewardService.nextApplyFlowLevelByName(clientName);
        }else{
            // 进入备选事件流, 删除ApplyItem
            // 注意：触发备选事件流后，自动删除ApplyItem, 这样client就可以立即重置申请页
            // 而ContractLink则需要由staff确认后手动删除，删除前确保client不能再次申请
            healthStewardService.removeApplyItemByName(clientName);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Staff-ContractConfirm
    @RequestMapping(path = "/staff/{staffName}/StaffContractConfirm", method = RequestMethod.GET)
    public ResponseEntity<Integer> getStaffContractConfirm(@PathVariable String clientName,
                                                           @PathVariable String staffName){
        int payload = contractLinkService.getStaffConfirmed(clientName, staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }



    @RequestMapping(path = "/StaffInfo", method = RequestMethod.GET)
    public ResponseEntity<SimpleStaffDTO> getStaffInfo(@PathVariable String clientName){
        SimpleStaffDTO payload = contractLinkService.getStaffInfo(clientName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(path = "/ContractResultTime", method = RequestMethod.GET)
    public ResponseEntity<String> getContractResultTime(@PathVariable String clientName){
        String payload = contractLinkService.getSignedTime(clientName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}

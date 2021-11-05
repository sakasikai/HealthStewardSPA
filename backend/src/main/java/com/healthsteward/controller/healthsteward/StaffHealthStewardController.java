package com.healthsteward.controller.healthsteward;

import com.healthsteward.pojo.DTO.healthsteward.*;
import com.healthsteward.service.interfaces.healthSteward.ContractLinkService;
import com.healthsteward.service.interfaces.healthSteward.HealthStewardService;
import com.healthsteward.service.interfaces.healthSteward.StaffNoteService;
import com.healthsteward.service.interfaces.user.ClientService;
import com.healthsteward.util.ZQTemp.StaffNoteTypeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/{staffName}/HealthSteward")
public class StaffHealthStewardController {
    private static final Logger logger = LoggerFactory.getLogger(StaffHealthStewardController.class);

    private final HealthStewardService healthStewardService;
    private final ContractLinkService contractLinkService;
    private final ClientService clientService;
    private final StaffNoteService staffNoteService;

    @Autowired
    public StaffHealthStewardController(HealthStewardService healthStewardService, ContractLinkService contractLinkService, ClientService clientService, StaffNoteService staffNoteService) {
        this.healthStewardService = healthStewardService;
        this.contractLinkService = contractLinkService;
        this.clientService = clientService;
        this.staffNoteService = staffNoteService;
    }

    // apply item list
    @RequestMapping(path = "/ApplyItems", method = RequestMethod.GET)
    public ResponseEntity<List<ApplyItemDTO> > listApplyItems(@PathVariable String staffName){
        List<ApplyItemDTO> payload = healthStewardService.listApplyItemDTOs();
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // ApplyFlowLevel
    @RequestMapping(path = "/{clientName}/ApplyFlowLevel", method = RequestMethod.GET)
    public ResponseEntity<Integer> getApplyFlowLevel(@PathVariable String staffName,
                                                     @PathVariable String clientName){
        Integer payload = healthStewardService.getApplyFlowLevelByName(clientName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // manage talking list
    @RequestMapping("/TalkingItems")
    public ResponseEntity<List<ContractItemDTO> > listTalkingItems(@PathVariable String staffName){
        List<ContractItemDTO> payload = contractLinkService.listContractItemDTOs(staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // manage signed clients
    @RequestMapping("/SignedClients")
    public ResponseEntity<List<SignedItemDTO> > listManagedClients(@PathVariable String staffName){
        List<SignedItemDTO> payload = contractLinkService.listValidSignedClients(staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // talk link creation
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(path = "/{clientName}/TalkLink", method= RequestMethod.PUT)
    public ResponseEntity<String> setTalkLinkConfirm(@PathVariable String staffName,
                                                     @PathVariable String clientName,
                                                     @RequestParam(value = "timeStamp", required = false, defaultValue = "2999-01-01-00:00:00")
                                                     @DateTimeFormat(pattern="yyyy-MM-dd-HH:mm:ss")
                                                     Date timeStamp){
        // 要有事务处理！！！！
        // 会创建一个TalkLink
        // 更新applyLevel
        String payload = "creation succeeded";
        if(contractLinkService.checkTalkLink(clientName)){
            payload = "creation failed";
            return new ResponseEntity<>(payload, HttpStatus.FORBIDDEN);
        }
        healthStewardService.nextApplyFlowLevelByName(clientName);
        contractLinkService.initContractLink(clientName, staffName, timeStamp);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // ContractLink
    @RequestMapping(path = "/{clientName}/StaffContractConfirm", method = RequestMethod.PUT)
    public ResponseEntity setStaffContractConfirm(@PathVariable String staffName,
                                                  @RequestParam int num,
                                                  @PathVariable String clientName){
        if(contractLinkService.setStaffConfirm(clientName, staffName, num)){
            // 每个flow正常结束后，都要改变level
            healthStewardService.nextApplyFlowLevelByName(clientName);
        }else{
            // 进入备选事件流, 删除ApplyItem
            // 注意：触发备选事件流后，自动删除ApplyItem, 这样client就可以立即重置申请页
            // 而ContractLink则需要由staff确认后手动删除，删除前确保client不能再次申请
//            healthStewardService.removeApplyItemByName(clientName);
        }
       return new ResponseEntity<>(HttpStatus.OK);
    }

    // Staff-ContractConfirm
    @RequestMapping(path = "/{clientName}/StaffContractConfirm", method = RequestMethod.GET)
    public ResponseEntity<Integer> getStaffContractConfirm(@PathVariable String staffName,
                                                           @PathVariable String clientName){
        int payload = contractLinkService.getStaffConfirmed(clientName, staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // Client-ContractConfirm
    @RequestMapping(path = "/{clientName}/ClientContractConfirm", method = RequestMethod.GET)
    public ResponseEntity<Integer> getClientContractConfirm(@PathVariable String staffName,
                                                            @PathVariable String clientName){
        Integer payload = contractLinkService.getClientConfirmed(clientName, staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // ContractState
    @RequestMapping("/{clientName}/ContractState")
    public ResponseEntity<Integer> getContractState(@PathVariable String staffName,
                                                    @PathVariable String clientName){

        // 用于初始化显示
        int payload = contractLinkService.getContractState(clientName, staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // contract info
    @RequestMapping(path = "/{clientName}/Contract", method = RequestMethod.PUT)
    public ResponseEntity setContractManaged(@PathVariable String staffName,
                                             @PathVariable String clientName){
        // contract正式生效！更新日期，managedState
        contractLinkService.setContractManaged(clientName, staffName);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    // contract info
    @RequestMapping("/{clientName}/ContractInformation")
    public ResponseEntity<ContractInfoDTO> getContractInfo(@PathVariable String staffName,
                                                           @PathVariable String clientName){
        // client调用
        ContractInfoDTO payload = contractLinkService.getContractInfo(clientName, staffName);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // 删除contract, 删除applyItem
    @RequestMapping(path = "/{clientName}/Contract", method = RequestMethod.DELETE)
    public ResponseEntity repealContract(@PathVariable String staffName,
                                         @PathVariable String clientName){
        contractLinkService.removeContractLink(clientName,staffName);
        // 删除对应ApplyItem, 这是它生命周期的终点
        healthStewardService.removeApplyItemByName(clientName);
        return new ResponseEntity(HttpStatus.OK);
    }


    // contract note
    @RequestMapping(path = "/{clientName}/Contract/{contractId}/{noteType}", method = RequestMethod.POST)
    public ResponseEntity<StaffNoteDTO> saveStaffNote(@PathVariable String staffName,
                                                      @PathVariable String noteType,
                                                      @RequestBody String note,
                                                      @PathVariable String clientName,
                                                      @PathVariable Integer contractId){
        int noteId = staffNoteService.saveStaffNote(staffName, clientName, note, StaffNoteTypeMatcher.parse(noteType), contractId);
        StaffNoteDTO payload = staffNoteService.getStaffNoteDTOById(staffName, clientName, noteId, StaffNoteTypeMatcher.parse(noteType));

        // set response headers
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = URI.create("http://xxx/"+noteType+"/"+noteId);
        headers.setLocation(locationUri);
        return new ResponseEntity<>(payload, headers, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{clientName}/{noteType}/{noteId}", method = RequestMethod.DELETE)
    public ResponseEntity removeStaffNote(@PathVariable String staffName,
                                          @PathVariable String noteType,
                                          @PathVariable String clientName,
                                          @PathVariable int noteId){
        staffNoteService.removeStaffNote(staffName, clientName, noteId, StaffNoteTypeMatcher.parse(noteType));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/{clientName}/{noteType}/{noteId}", method = RequestMethod.PUT)
    public ResponseEntity updateStaffNote(@PathVariable String staffName,
                                          @PathVariable String noteType,
                                          @RequestBody String note,
                                          @PathVariable String clientName,
                                          @PathVariable int noteId){
        staffNoteService.setStaffNote(staffName, clientName, note, noteId, StaffNoteTypeMatcher.parse(noteType));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/{noteType}", method = RequestMethod.GET)
    public ResponseEntity<List<StaffNoteDTO> > listStaffNote(@PathVariable String staffName,
                                                             @PathVariable String noteType) {

        List<StaffNoteDTO> payload = staffNoteService.listStaffNote(staffName,null, StaffNoteTypeMatcher.parse(noteType));
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(path = "/{clientName}/{noteType}", method = RequestMethod.GET)
    public ResponseEntity<List<StaffNoteDTO> > getStaffNote(@PathVariable String staffName,
                                                            @PathVariable String noteType,
                                                            @PathVariable String clientName) {
        List<StaffNoteDTO> payload = staffNoteService.listStaffNote(staffName, clientName, StaffNoteTypeMatcher.parse(noteType));
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}

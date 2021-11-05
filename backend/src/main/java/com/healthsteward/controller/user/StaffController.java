package com.healthsteward.controller.user;

import com.healthsteward.pojo.DO.user.Staff;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.pojo.DTO.user.StaffDTO;
import com.healthsteward.service.interfaces.user.StaffService;
import com.healthsteward.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;

@RestController
@RequestMapping("/staff")
public class StaffController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/{username}/get/name")
    public ResponseEntity<String> getStaffNameByUsername(@PathVariable String username){
        logger.info("staff "+username+" get name");
        String s = staffService.getStaffNameByUsername(username);
        if(s != null){
            logger.info("success "+s);
            return ResponseEntity.ok(s);
        }
        else{
            logger.info("fail");
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{username}/get/simpleUserInfo")
    public ResponseEntity<SimpleUserDTO> getSimpleUserInfoByUsername(@PathVariable String username){
        logger.info("staff "+username+" get staffInfo");
        Staff staff = staffService.getStaffByUserName(username);
        if(staff != null){
            logger.info("success "+staff);
            return ResponseEntity.ok(staff.toSimpleUserDTO());
        }
        else{
            logger.info("fail");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{username}/get/staffInfo")
    public ResponseEntity<StaffDTO> getStaffByUsername(@PathVariable String username){
        logger.info("staff "+username+" get staffInfo");
        Staff staff = staffService.getStaffByUserName(username);
        if(staff != null){
            logger.info("success "+staff);
            return ResponseEntity.ok(staff.toStaffDTO());
        }
        else{
            logger.info("fail");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{username}/update")
    public ResponseEntity<String> updateStaffInfo(@PathVariable String username, @RequestBody StaffDTO staffDTO){
        logger.info("update staff "+staffDTO);
        boolean b = staffService.updateStaff(staffDTO.toStaff());
        if(b){
            logger.info("success");
            return ResponseEntity.ok("");
        }
        else{
            logger.info("fail");
            return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/{username}/image/update")
    public ResponseEntity<String> updateStaffImage(@PathVariable String username, @RequestPart(value="file") MultipartFile file){
        logger.info(username+" update staff image");
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            String image = encoder.encode(file.getBytes());
            boolean b = staffService.updateStaffImage(username,image);
            if(b) {
                logger.info("success");
                return ResponseEntity.ok("");
            }
            else {
                logger.info("fail");
                return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/image/get")
    public ResponseEntity<byte[]> getStaffImage(@PathVariable String username) {
        byte[] imageBytes = (byte[])staffService.getStaffImage(username);
        try{
            return ImageUtils.decodeAndGetResponseEntity(imageBytes);
        }
        catch (IOException e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

package com.healthsteward.controller.user;

import com.healthsteward.pojo.DO.user.Client;
import com.healthsteward.pojo.DTO.auth.SimpleUserDTO;
import com.healthsteward.pojo.DTO.user.ClientDTO;
import com.healthsteward.service.interfaces.user.ClientService;
import com.healthsteward.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;


@RestController
@RequestMapping("/client")
public class ClientController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{username}/get/simpleUserInfo")
    public ResponseEntity<SimpleUserDTO> getSimpleUserInfoByUsername(@PathVariable String username) throws Exception {
        Client client = clientService.getClient(username);
        if(client != null)
            return ResponseEntity.ok(client.toSimpleUserDTO());
        else
            throw new Exception("client username 不存在");
    }

    @GetMapping("/{username}/get/clientInfo")
    public ResponseEntity<ClientDTO> getClientByUsername(@PathVariable String username) throws Exception {
        Client client = clientService.getClient(username);
        if(client != null)
            return ResponseEntity.ok(client.toClientDTO());
        else
            throw new Exception("client username 不存在");
    }

    @PostMapping("/{username}/update")
    public ResponseEntity<String> updateClientInfo(@RequestBody ClientDTO clientDTO,
                                                   @PathVariable String username){
//        logger.debug("update client "+clientDTO);
        boolean b = clientService.updateClient(clientDTO.toClient());
        if(b){
            return ResponseEntity.ok("");
        }
        else{
            return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/image/update/{clientId}")
    public ResponseEntity<String> updateClientImage(@RequestPart(value = "file") MultipartFile file, @PathVariable("clientId") Integer clientId){
        try{
            BASE64Encoder encoder = new BASE64Encoder();
            String image = encoder.encode(file.getBytes());
            clientService.updateClientImageByUserId(clientId,image);
            return ResponseEntity.ok("");
        }catch(IOException e){
            return new ResponseEntity<>("", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/image/get/{clientId}")
    public ResponseEntity<byte[]> getClientImage(@PathVariable Integer clientId){
        byte[] imageBytes = (byte[]) clientService.getClientImageByUserId(clientId);
        try{
            return ImageUtils.decodeAndGetResponseEntity(imageBytes);
        }catch(IOException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

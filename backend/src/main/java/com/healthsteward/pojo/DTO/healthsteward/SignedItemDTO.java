package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SignedItemDTO {
    Integer contractId;
    String clientName;
    String nickName;
    String creationTimeStamp;
}

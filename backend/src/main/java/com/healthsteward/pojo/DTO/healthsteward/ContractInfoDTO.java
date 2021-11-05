package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ContractInfoDTO {
    private String staffName;
    private String clientName;
    private Integer contractState;
    private Date creationTimeStamp;
    private Date resultTimeStamp;
    private String resultComment;
}

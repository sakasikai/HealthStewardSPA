package com.healthsteward.pojo.DO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractLink {
    private Integer contractId;
    private Integer clientId;
    private Integer staffId;
    private Integer clientConfirm;
    private Integer staffConfirm;
    private Date creationTimeStamp;
    private Date resultTimeStamp;
    private Integer contractState;
    private String resultComment;
    private Integer managedState;

}

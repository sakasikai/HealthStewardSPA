package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ContractItemDTO { // 管理洽谈列表
    Integer contractId;
    String clientName;
    String nickName;
    String creationTimeStamp;
    Integer clientConfirm;
    Integer staffConfirm;
    Integer contractState;
}

package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApplyItemDTO { // 返回对象
    Integer applyItemId;
    String userName;
    String nickName;
    String timeStamp;
    String applyNote;
    Integer applyState; // 0-5; ApplyFlowLevel 2为可以申请
}

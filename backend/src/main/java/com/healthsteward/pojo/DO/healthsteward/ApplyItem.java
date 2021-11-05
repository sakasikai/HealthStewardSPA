package com.healthsteward.pojo.DO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyItem {
    private Integer applyItemId;
    private Integer clientId;
    private Date timeStamp;
    private String applyNote;
    private Integer flowLevel;
}

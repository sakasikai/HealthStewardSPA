package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApplyCreationDTO {
    private String timeStamp;
    private String applyNote;
    private Integer flowLevel;
}

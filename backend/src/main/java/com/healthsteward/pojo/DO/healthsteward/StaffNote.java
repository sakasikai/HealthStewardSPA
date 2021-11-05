package com.healthsteward.pojo.DO.healthsteward;

import lombok.Data;

import java.util.Date;

@Data
public class StaffNote {
    private Integer noteId;
    private Integer staffId;
    private Integer clientId;
    private String staffNote;
    private Date timeStamp;
}

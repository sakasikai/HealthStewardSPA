package com.healthsteward.pojo.DTO.healthsteward;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffNoteDTO {
    private Integer noteId;
    private String staffName;
    private String clientName;
    private String staffNote;
    private String timeStamp;
}

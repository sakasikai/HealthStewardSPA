package com.healthsteward.service.interfaces.healthSteward;

import com.healthsteward.pojo.DTO.healthsteward.StaffNoteDTO;
import com.healthsteward.util.ZQTemp.StaffNoteType;

import java.util.List;

public interface StaffNoteService {
    public int saveStaffNote(String staffName, String clientName, String note, StaffNoteType noteType, int contractId);
    public void removeStaffNote(String staffName, String clientName, int noteId, StaffNoteType noteType);
    public void setStaffNote(String staffName, String clientName, String note, int noteId, StaffNoteType noteType);
    public StaffNoteDTO getStaffNoteDTOById(String staffName, String clientName, int noteId, StaffNoteType noteType);
    public List<StaffNoteDTO> listStaffNote(String staffName, String clientName, StaffNoteType noteType); // clientName 为 null 选全部
}

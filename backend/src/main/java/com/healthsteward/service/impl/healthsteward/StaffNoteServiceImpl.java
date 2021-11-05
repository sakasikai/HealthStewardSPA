package com.healthsteward.service.impl.healthsteward;

import com.healthsteward.mapper.auth.UserMapper;
import com.healthsteward.mapper.healthsteward.StaffNoteMapper;
import com.healthsteward.pojo.DO.healthsteward.StaffNote;
import com.healthsteward.pojo.DTO.healthsteward.StaffNoteDTO;
import com.healthsteward.service.interfaces.healthSteward.StaffNoteService;
import com.healthsteward.util.DateFormat;
import com.healthsteward.util.ZQTemp.StaffNoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffNoteServiceImpl implements StaffNoteService {

    private final UserMapper userMapper;
    private final StaffNoteMapper staffNoteMapper;

    @Autowired
    public StaffNoteServiceImpl(UserMapper userMapper, StaffNoteMapper staffNoteMapper) {
        this.userMapper = userMapper;
        this.staffNoteMapper = staffNoteMapper;
    }

    @Override
    public int saveStaffNote(String staffName, String clientName, String note, StaffNoteType noteType, int contractId) {
        Map<String, Object> params = new HashMap<>();
        Date timeStamp = new Date();
        int staffId = userMapper.getIdByName(staffName);
        int clientId = userMapper.getIdByName(clientName);
        params.put("staffId", staffId);
        params.put("clientId", clientId);
        params.put("staffNote", note);
        params.put("timeStamp", timeStamp);
        params.put("noteTypeOrdinal", noteType.ordinal());
        params.put("contractId", contractId);
        staffNoteMapper.insertStaffNote(params);
        return staffNoteMapper.getStaffNoteId(staffId, clientId, timeStamp, noteType.ordinal());
    }

    @Override
    public void removeStaffNote(String staffName, String clientName, int id, StaffNoteType noteType) {
        int staffId = userMapper.getIdByName(staffName);
        int clientId = userMapper.getIdByName(clientName);
        staffNoteMapper.deleteStaffNote(staffId, clientId, id, noteType.ordinal());
    }

    @Override
    public void setStaffNote(String staffName, String clientName, String note, int id, StaffNoteType noteType) {
        Map<String, Object> params = new HashMap<>();
        params.put("staffId", userMapper.getIdByName(staffName));
        params.put("clientId", userMapper.getIdByName(clientName));
        params.put("staffNote", note);
        params.put("timeStamp", new Date());
        params.put("noteId", id);
        params.put("noteTypeOrdinal", noteType.ordinal());
        staffNoteMapper.updateStaffNote(params);
    }

    @Override
    public StaffNoteDTO getStaffNoteDTOById(String staffName, String clientName, int noteId, StaffNoteType noteType) {
        int staffId = userMapper.getIdByName(staffName);
        int clientId = userMapper.getIdByName(clientName);
        StaffNote staffNote = staffNoteMapper.getStaffNoteById(staffId, clientId, noteId, noteType.ordinal());
        StaffNoteDTO dto = new StaffNoteDTO(noteId, staffName, clientName,
                staffNote.getStaffNote(), DateFormat.format(staffNote.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
        return dto;
    }

    @Override
    public List<StaffNoteDTO> listStaffNote(String staffName, String clientName, StaffNoteType noteType) {
        int staffId = userMapper.getIdByName(staffName);
        Integer clientId = null;
        if(clientName!=null){
            clientId = userMapper.getIdByName(clientName);
        }
        List<StaffNote> noteList = staffNoteMapper.listStaffNote(staffId, clientId, noteType.ordinal());
        List<StaffNoteDTO> dtos = new LinkedList<>();
        for (StaffNote staffNote : noteList) {
            StaffNoteDTO dto = new StaffNoteDTO(
                    staffNote.getNoteId(),
                    staffName,
                    userMapper.getNameById(staffNote.getClientId()),
                    staffNote.getStaffNote(),
                    DateFormat.format(staffNote.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
            dtos.add(dto);
        }
        return dtos;
    }
}

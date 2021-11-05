package com.healthsteward.mapper.healthsteward;

import com.healthsteward.pojo.DO.healthsteward.StaffNote;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface StaffNoteMapper {
    public void insertStaffNote(Map params); // (String note, Date timeStamp) subject to (int Id, int staffId,int clientId)

    public void deleteStaffNote(@Param("staffId") int staffId, @Param("clientId") int clientId, @Param("noteId") int noteId, @Param("noteTypeOrdinal") int noteTypeOrdinal);

    public void updateStaffNote(Map params);

    public List<StaffNote> listStaffNote(@Param("staffId") int staffId, @Param("clientId") Integer clientId, @Param("noteTypeOrdinal") int noteTypeOrdinal);

    public Integer getStaffNoteId(@Param("staffId") int staffId, @Param("clientId") int clientId, @Param("timeStamp") Date timeStamp, @Param("noteTypeOrdinal") int noteTypeOrdinal);

    public StaffNote getStaffNoteById(@Param("staffId") int staffId, @Param("clientId") int clientId, @Param("noteId") int id, @Param("noteTypeOrdinal") int noteTypeOrdinal);
}


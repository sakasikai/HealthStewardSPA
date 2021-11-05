package com.healthsteward.mapper.healthsteward;

import com.healthsteward.pojo.DO.healthsteward.ContractLink;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ContractLinkMapper {
    // CRUD操作：
    public void insertContractLink(@Param("clientId") int clientId, @Param("staffId") int staffId, @Param("timeStamp") Date timeStamp);
    public void deleteContractLink(@Param("clientId") int clientId, @Param("staffId") int staffId);

    public void updateContractLinkByMap(Map<String, Object> map);
    public void updateClientConfirm(@Param("clientId") int clientId, @Param("staffId") int staffId, @Param("clientConfirm") int option);
    public void updateStaffConfirm(@Param("clientId") int clientId, @Param("staffId") int staffId, @Param("staffConfirm") int option);
    public void updateContractState(@Param("clientId") int clientId, @Param("staffId") int staffId, @Param("contractState") int state);
//    public void updateStaffNote(@Param("clientId") int clientId, @Param("staffId") int staffId, String note);

    public ContractLink getContractLink(@Param("clientId") int clientId, @Param("staffId") Integer staffId);
    public List<ContractLink> listContractLink(@Param("staffId") int staffId);
    public List<ContractLink> listValidSignedClients(@Param("staffId") int staffId);
}

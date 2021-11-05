package com.healthsteward.mapper.healthsteward;

import com.healthsteward.pojo.DO.healthsteward.ApplyItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HealthStewardMapper {
    // CRUD操作：
    public void insertApplyItem(Map map);
    public void deleteApplyItemById(int clientId);
    public void updateFlowLevelByClientId(@Param("clientId") int clientId, @Param("flowLevel") int level);

    public ApplyItem getApplyItemByClientId(int clientId);
    public List<ApplyItem> listApplyItems(boolean allLifeCircle); // true则查询全部ApplyItems, false只返回 flowLevel<3 的ApplyItems
}

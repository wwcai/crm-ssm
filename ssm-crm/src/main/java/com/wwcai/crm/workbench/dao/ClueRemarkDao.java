package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListById(String clueId);

    int delete(ClueRemark clueRemark);

    int saveRemark(ClueRemark cr);

    int deleteRemark(String id);

    int updateRemark(ClueRemark cr);
}

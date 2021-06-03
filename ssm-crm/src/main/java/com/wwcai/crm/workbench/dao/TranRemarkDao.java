package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkDao {
    int save(TranRemark tr);

    int deleteRemark(String id);

    int updateRemark(TranRemark tr);

    List<TranRemark> getRemarkListByTid(String tranId);
}

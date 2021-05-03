package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getHistoryByTranId(String tranId);
}

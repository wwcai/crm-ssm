package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranListByCondition(Map<String, Object> map);

    Tran getTranById(String id);

    int update(Tran t);

    List<Tran> getTranListByCustomerId(String customerId);

    List<Tran> getTranListByContactsId(String contactsId);

    int delete(String[] ids);
}

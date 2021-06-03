package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.domain.TranHistory;
import com.wwcai.crm.workbench.domain.TranRemark;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getChrats();

    PaginationVo<Tran> pageList(Map<String, Object> map, String customerName, String contactsName);

    Map<String, Object> getUserListAndTran(String id);

    boolean update(Tran t, String customerName);

    List<TranRemark> getRemarkListByTid(String tranId);

    boolean saveRemark(TranRemark tr);

    boolean deleteRemark(String id);

    boolean updateRemark(TranRemark tr);

    Boolean delete(String[] ids);
}

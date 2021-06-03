package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Clue;
import com.wwcai.crm.workbench.domain.ClueRemark;
import com.wwcai.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue clue);

    boolean delete(String[] ids);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

    List<ClueRemark> getRemarkListByCid(String clueId);

    boolean saveRemark(ClueRemark cr);

    boolean deleteRemark(String id);

    boolean updateRemark(ClueRemark cr);

    Map<String, Object> getChrats();
}

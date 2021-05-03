package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getClueById(String id);

    int update(Clue clue);

    int delete(String[] ids);

    int delete1(String clueId);
}

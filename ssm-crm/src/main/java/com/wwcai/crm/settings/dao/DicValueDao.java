package com.wwcai.crm.settings.dao;

import com.wwcai.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}

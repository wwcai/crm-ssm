package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactsByName(String cname);

    String getContactsIdByName(String contactsName);

    int getTotalByCondition(Map<String, Object> map);

    List<Contacts> getCustomerByCondition(Map<String, Object> map);

    Contacts detail(String id);

    Contacts getContactsById(String id);

    int update(Contacts c);
}

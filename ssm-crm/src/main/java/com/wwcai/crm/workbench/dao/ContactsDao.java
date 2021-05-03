package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactsByName(String cname);

    String getContactsIdByName(String contactsName);
}

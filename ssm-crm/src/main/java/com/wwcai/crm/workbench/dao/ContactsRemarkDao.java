package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {

    int save(ContactsRemark contactsRemark);

    List<ContactsRemark> getRemarkListByCid(String contactsId);

    int deleteRemark(String id);

    int updateRemark(ContactsRemark cr);
}

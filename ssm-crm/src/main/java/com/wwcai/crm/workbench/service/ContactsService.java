package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    List<Contacts> getContactsByName(String cname);

    PaginationVo<Contacts> pageList(Map<String, Object> map);

    boolean save(Contacts c, String customerName);

    Contacts detail(String id);

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts c, String customerName);

}

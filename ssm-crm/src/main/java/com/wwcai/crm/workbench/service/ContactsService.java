package com.wwcai.crm.workbench.service;

import com.wwcai.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> getContactsByName(String cname);
}

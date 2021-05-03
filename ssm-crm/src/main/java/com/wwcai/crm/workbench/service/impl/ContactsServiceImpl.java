package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.workbench.dao.ContactsDao;
import com.wwcai.crm.workbench.dao.ContactsRemarkDao;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.service.ContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;

    @Override
    public List<Contacts> getContactsByName(String cname) {

        List<Contacts> clist = contactsDao.getContactsByName(cname);

        return clist;
    }
}

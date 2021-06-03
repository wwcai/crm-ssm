package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.ClueRemark;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.domain.ContactsRemark;
import com.wwcai.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    List<Contacts> getContactsByName(String cname);

    PaginationVo<Contacts> pageList(Map<String, Object> map);

    boolean save(Contacts c, String customerName);

    Contacts detail(String id);

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts c, String customerName);

    Boolean delete(String[] ids);

    List<ContactsRemark> getRemarkListByCid(String contactsId);

    boolean saveRemark(ContactsRemark cr);

    boolean deleteRemark(String id);

    boolean updateRemark(ContactsRemark cr);

    List<Tran> getTranListByContactsId(String contactsId);

    boolean bund(String cid, String[] aids);

    boolean unbund(String id);
}

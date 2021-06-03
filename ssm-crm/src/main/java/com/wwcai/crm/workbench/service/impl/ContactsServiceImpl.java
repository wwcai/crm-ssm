package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.*;
import com.wwcai.crm.workbench.domain.*;
import com.wwcai.crm.workbench.service.ContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Override
    public List<Contacts> getContactsByName(String cname) {

        List<Contacts> clist = contactsDao.getContactsByName(cname);

        return clist;
    }

    @Override
    public PaginationVo<Contacts> pageList(Map<String, Object> map) {

        int total = contactsDao.getTotalByCondition(map);

        List<Contacts> datalist = contactsDao.getCustomerByCondition(map);

        PaginationVo<Contacts> vo = new PaginationVo<>();
        vo.setDatalist(datalist);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public boolean save(Contacts c, String customerName) {
        boolean flag = true;

        String customerId = customerDao.getCustomerIdByName(customerName);
        if (customerId != null)
            c.setCustomerId(customerId);

        int count = contactsDao.save(c);
        if(count != 1)
            flag = false;

        return flag;
    }

    @Override
    public Contacts detail(String id) {

        return contactsDao.detail(id);
    }

    @Override
    public Map<String, Object> getUserListAndContacts(String id) {

        List<User> ulist = userDao.getUserList();

        Contacts c = contactsDao.getContactsById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("ulist", ulist);
        map.put("c", c);

        return map;
    }

    @Override
    public boolean update(Contacts c, String customerName) {
        Boolean flag = true;

        String customerId = customerDao.getCustomerIdByName(customerName);
        if (customerId != null)
            c.setCustomerId(customerId);

        int count = contactsDao.update(c);
        if(count != 1)
            flag = false;

        return flag;
    }

    @Override
    public Boolean delete(String[] ids) {

        Boolean flag = true;

        int count = contactsDao.delete(ids);
        if(count < 1)
            flag = false;


        return flag;
    }

    @Override
    public List<ContactsRemark> getRemarkListByCid(String contactsId) {

        List<ContactsRemark> clist = contactsRemarkDao.getRemarkListByCid(contactsId);

        return clist;
    }

    @Override
    public boolean saveRemark(ContactsRemark cr) {

        Boolean flag = true;

        int count = contactsRemarkDao.save(cr);
        if (count != 1)
            flag = false;

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        Boolean flag = true;

        int count = contactsRemarkDao.deleteRemark(id);
        if (count != 1)
            flag = false;

        return flag;
    }

    @Override
    public boolean updateRemark(ContactsRemark cr) {

        Boolean flag = true;

        int count = contactsRemarkDao.updateRemark(cr);

        if (count != 1)
            flag = false;

        return flag;
    }

    @Override
    public List<Tran> getTranListByContactsId(String contactsId) {

        List<Tran> tlist = tranDao.getTranListByContactsId(contactsId);

        return tlist;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;

        for(String aid : aids) {

            // 取得每一个aid与cid做关联
            ContactsActivityRelation car = new ContactsActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setContactsId(cid);

            int count = contactsActivityRelationDao.save(car);
            if(count < 1) {
                flag = false;
            }

        }


        return flag;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = contactsActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

}

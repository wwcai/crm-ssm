package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.ContactsDao;
import com.wwcai.crm.workbench.dao.CustomerDao;
import com.wwcai.crm.workbench.dao.CustomerRemarkDao;
import com.wwcai.crm.workbench.dao.TranDao;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.domain.Customer;
import com.wwcai.crm.workbench.domain.CustomerRemark;
import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;
    @Resource
    private UserDao userDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private ContactsDao contactsDao;

    @Override
    public List<String> getCustomerName(String name) {

        List<String> slist = customerDao.getCustomerName(name);

        return slist;
    }

    @Override
    public PaginationVo<Customer> pageList(Map<String, Object> map) {

        int total = customerDao.getTotalByCondition(map);

        List<Customer> datalist = customerDao.getCustomerByCondition(map);

        PaginationVo<Customer> vo = new PaginationVo<>();
        vo.setDatalist(datalist);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        int count = customerDao.delete(ids);

        if (count < 1)
            flag = false;

        return flag;
    }

    @Override
    public boolean save(Customer c) {

        boolean flag = true;

        int count = customerDao.save(c);
        if (count != 1)
            flag = false;

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndCustomer(String id) {

        List<User> ulist = userDao.getUserList();

        Customer c = customerDao.getCustomerById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("ulist", ulist);
        map.put("c", c);


        return map;
    }

    @Override
    public boolean update(Customer c) {
        boolean flag = true;

        int count = customerDao.update(c);
        if (count != 1)
            flag = false;

        return flag;
    }

    @Override
    public Customer detail(String id) {

        Customer c = customerDao.detail(id);

        return c;
    }

    @Override
    public String getCustomerIdByName(String customerName) {


        return customerDao.getCustomerIdByName(customerName);
    }

    @Override
    public boolean deleteRemark(String id) {

        Boolean flag = true;

        int count = customerRemarkDao.deleteRemark(id);
        if (count != 1)
            flag = false;
        return flag;
    }

    @Override
    public boolean saveRemark(CustomerRemark cr) {

        Boolean flag = true;

        int count = customerRemarkDao.save(cr);
        if (count != 1)
            flag = false;
        return flag;

    }

    @Override
    public List<CustomerRemark> getRemarkListByCid(String customerId) {

        List<CustomerRemark> clist = customerRemarkDao.getRemarkListByCid(customerId);

        return clist;
    }

    @Override
    public boolean updateRemark(CustomerRemark cr) {

        Boolean flag = true;

        int count = customerRemarkDao.updateRemark(cr);
        if (count != 1)
            flag = false;
        return flag;

    }

    @Override
    public List<Tran> getTranListByCustomerId(String customerId) {

        List<Tran> tlist = tranDao.getTranListByCustomerId(customerId);

        return tlist;
    }

    @Override
    public List<Contacts> getContactsListByCustomerId(String customerId) {

        List<Contacts> clist = contactsDao.getContactsListByCustomerId(customerId);

        return clist;
    }

    @Override
    public Map<String, Object> getChrats() {

        int total = customerDao.getTotal();

        // 取得dataList
        List<Map<String, Object>> dataList = customerDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);

        return map;

    }
}

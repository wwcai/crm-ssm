package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.*;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<String> getCustomerName(String name);

    PaginationVo<Customer> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    boolean save(Customer c);

    Map<String, Object> getUserListAndCustomer(String id);

    boolean update(Customer c);

    Customer detail(String id);

    String getCustomerIdByName(String customerName);

    boolean deleteRemark(String id);

    boolean saveRemark(CustomerRemark cr);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    boolean updateRemark(CustomerRemark cr);

    List<Tran> getTranListByCustomerId(String customerId);

    List<Contacts> getContactsListByCustomerId(String customerId);

    Map<String, Object> getChrats();
}

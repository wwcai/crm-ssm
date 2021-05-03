package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Customer;

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
}

package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);

    int getTotalByCondition(Map<String, Object> map);

    List<Customer> getCustomerByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Customer getCustomerById(String id);

    int update(Customer c);

    Customer detail(String id);

    String getCustomerIdByName(String customerName);

    int getTotal();

    List<Map<String, Object>> getCharts();
}

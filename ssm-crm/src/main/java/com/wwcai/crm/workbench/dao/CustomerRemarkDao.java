package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    int updateRemark(CustomerRemark cr);

    int deleteRemark(String id);
}

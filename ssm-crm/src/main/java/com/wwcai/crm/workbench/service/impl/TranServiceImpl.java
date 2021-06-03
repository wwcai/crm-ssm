package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.*;
import com.wwcai.crm.workbench.domain.*;
import com.wwcai.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TranRemarkDao tranRemarkDao;

    @Override
    public boolean save(Tran t, String customerName) {

        /*
            交易添加业务
                在添加之前，t里面少了customerId
                处理客户相关需求
                    有 则取Id
                    没有则新建 再取Id

                添加交易成功后，创建一条交易历史


         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if(cus == null) {

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateTime(t.getCreateTime());
            cus.setCreateBy(t.getCreateBy());
            cus.setContactSummary(t.getContactSummary());
            cus.setOwner(t.getOwner());
            cus.setNextContactTime(t.getNextContactTime());

            int count1 = customerDao.save(cus);
            if(count1 != 1)
                flag = false;
        }

        // 已得到客户Id customerId
        t.setCustomerId(cus.getId());

        int count2 = tranDao.save(t);
        if(count2 != 1)
            flag = false;

        // 添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());

        int count3 = tranHistoryDao.save(th);
        if(count3 != 1)
            flag = false;

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String tranId) {

        List<TranHistory> thlist = tranHistoryDao.getHistoryByTranId(tranId);

        return thlist;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        // 改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1 != 1)
            flag = false;

        // 交易阶段改变后生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setTranId(t.getId());
        // 添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2 != 1)
            flag = false;

        return flag;
    }

    @Override
    public Map<String, Object> getChrats() {

        // 取得total
        int total = tranDao.getTotal();

        // 取得dataList
        List<Map<String, Object>> dataList = tranDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);



        return map;
    }

    @Override
    public PaginationVo<Tran> pageList(Map<String, Object> map, String customerName, String contactsName) {

        String contactsId = null;
        String customerId = null;
        if (customerName != null || contactsName != null) {
            // 根据客户名称去客户 id
            customerId = customerDao.getCustomerIdByName(customerName);

            // 根据联系人名称去联系人 id
            contactsId = contactsDao.getContactsIdByName(contactsName);
            map.put("contactsId", contactsId);
        }

        map.put("customerId", customerId);
        map.put("contactsId", contactsId);

        // 取total
        int total = tranDao.getTotalByCondition(map);

        // 取datalist
        List<Tran> datalist = tranDao.getTranListByCondition(map);

        PaginationVo<Tran> vo = new PaginationVo<Tran>();
        vo.setTotal(total);
        vo.setDatalist(datalist);

        return vo;
    }

    @Override
    public Map<String, Object> getUserListAndTran(String id) {

        // 取ulist
        List<User> ulist = userDao.getUserList();

        // 取 c
        Tran t = tranDao.getTranById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("ulist", ulist);
        map.put("t", t);

        return map;
    }

    @Override
    public boolean update(Tran t, String customerName) {
        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if(cus == null) {

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setEditTime(t.getEditTime());
            cus.setEditBy(t.getEditBy());
            cus.setContactSummary(t.getContactSummary());
            cus.setOwner(t.getOwner());
            cus.setNextContactTime(t.getNextContactTime());

            int count1 = customerDao.save(cus);
            if(count1 != 1)
                flag = false;
        }

        // 已得到客户Id customerId
        t.setCustomerId(cus.getId());

        int count2 = tranDao.update(t);
        if(count2 != 1)
            flag = false;


        return flag;
    }

    @Override
    public List<TranRemark> getRemarkListByTid(String tranId) {

        List<TranRemark> tlist = tranRemarkDao.getRemarkListByTid(tranId);

        return tlist;
    }

    @Override
    public boolean saveRemark(TranRemark tr) {

        Boolean flag = true;

        int count = tranRemarkDao.save(tr);
        if(count != 1)
            flag = false;
        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        Boolean flag = true;

        int count = tranRemarkDao.deleteRemark(id);
        if(count != 1)
            flag = false;
        return flag;

    }

    @Override
    public boolean updateRemark(TranRemark tr) {

        Boolean flag = true;

        int count = tranRemarkDao.updateRemark(tr);
        if(count != 1)
            flag = false;
        return flag;
    }

    @Override
    public Boolean delete(String[] ids) {

        boolean flag = true;

        int count = tranDao.delete(ids);
        if (count < 1)
            flag = false;

        return flag;
    }
}

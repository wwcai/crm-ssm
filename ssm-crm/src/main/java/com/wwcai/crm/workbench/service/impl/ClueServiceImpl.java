package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.*;
import com.wwcai.crm.workbench.domain.*;
import com.wwcai.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;


    @Override
    public boolean save(Clue clue) {

        boolean flag = true;

        int count =clueDao.save(clue);
        if(count != 1) {
            flag = false;
        }


        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Object> map) {

        // 取total
        int total = clueDao.getTotalByCondition(map);

        // 取datalist
        List<Clue> datalist = clueDao.getClueListByCondition(map);

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDatalist(datalist);

        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);


        return c;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {

        // 取ulist
        List<User> ulist = userDao.getUserList();

        // 取 c
        Clue c = clueDao.getClueById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("ulist", ulist);
        map.put("c", c);


        return map;
    }

    @Override
    public boolean update(Clue clue) {

        boolean flag = true;

        int count = clueDao.update(clue);

        if(count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        int count = clueDao.delete(ids);

        if(count < 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for(String aid : aids) {

            // 取得每一个aid与cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            int count = clueActivityRelationDao.bund(car);
            if(count < 1) {
                flag = false;
            }

        }


        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        // (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getClueById(clueId);


        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);

        if(cus == null) {

            cus = new Customer();
            cus.setWebsite(c.getWebsite());
            cus.setAddress(c.getAddress());
            cus.setContactSummary(c.getContactSummary());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setDescription(c.getDescription());
            cus.setId(UUIDUtil.getUUID());
            cus.setName(company);
            cus.setPhone(c.getPhone());
            cus.setCtype("新客户");
            cus.setNextContactTime(c.getNextContactTime());
            cus.setOwner(c.getOwner());

            int count1 = customerDao.save(cus);
            if(count1 != 1) {
                flag = false;
            }

        }
        // (3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());
        con.setContactSummary(c.getContactSummary());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setCustomerId(cus.getId());
        con.setDescription(c.getDescription());
        con.setEmail(c.getEmail());
        con.setFullname(c.getFullname());
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setJob(c.getJob());
        con.setMphone(c.getMphone());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());

        int count2 = contactsDao.save(con);
        if(count2 != 1) {
            flag = false;
        }

        // (4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getListById(clueId);
        for(ClueRemark clueRemark : clueRemarks) {

            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");

            int count3 = customerRemarkDao.save(customerRemark);
            if(count3 != 1) {
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);

            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4 != 1) {
                flag = false;
            }

        }

        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelations =
                clueActivityRelationDao.getListByCludId(clueId);
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations) {

            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation =
                    new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setActivityId(activityId);

            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5 != 1) {
                flag = false;
            }
        }

        // (6) 如果有创建交易需求，创建一条交易
        if(t.getId() != null) {
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());

            int count6 = tranDao.save(t);
            if(count6 != 1) {
                flag = false;
            }


            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setTranId(t.getId());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setId(UUIDUtil.getUUID());
            th.setExpectedDate(t.getExpectedDate());
            th.setCreateTime(createTime);
            th.setCreateBy(createBy);


            int count7 = tranHistoryDao.save(th);
            if(count7 != 1) {
                flag = false;
            }
        }


        // (8) 删除线索备注
        for(ClueRemark clueRemark : clueRemarks){
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8 != 1) {
                flag = false;
            }
        }
        // (9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1) {
                flag = false;
            }
        }
        // (10) 删除线索
        int count10 = clueDao.delete1(clueId);
        if(count10 != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public List<ClueRemark> getRemarkListByCid(String clueId) {

        List<ClueRemark> cList = clueRemarkDao.getListById(clueId);

        return cList;
    }

    @Override
    public boolean saveRemark(ClueRemark cr) {

        boolean flag = true;

        int count = clueRemarkDao.saveRemark(cr);

        if(count != 1)
            flag = false;

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = clueRemarkDao.deleteRemark(id);

        if(count != 1)
            flag = false;

        return flag;
    }

    @Override
    public boolean updateRemark(ClueRemark cr) {
        boolean flag = true;

        int count = clueRemarkDao.updateRemark(cr);

        if(count != 1)
            flag = false;

        return flag;
    }

    @Override
    public Map<String, Object> getChrats() {

        int total = clueDao.getTotal();

        List<Map<String, Object>> dataList = clueDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);

        return map;
    }
}

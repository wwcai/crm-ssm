package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.ActivityDao;
import com.wwcai.crm.workbench.dao.ActivityRemarkDao;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.domain.ActivityRemark;
import com.wwcai.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;
    @Resource
    private UserDao userDao;


    @Override
    public boolean save(Activity a) {

        boolean falg = true;

        int count = activityDao.save(a);
        if(count != 1) {
            falg = false;
        }
        return falg;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        // 取得total
        int total = activityDao.getTotalByCondition(map);

        // 取得datalist
        List<Activity> datalist = activityDao.getActivityListByCondition(map);

        // 将total和datalsit封装到vo中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDatalist(datalist);

        // 返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean falg = true;
        // 查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        // 删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByIds(ids);

        if(count1 != count2) {
            falg = false;
        }

        // 删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length) {
            falg = false;
        }

        return falg;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        // 取uLIst
        List<User> uList = userDao.getUserList();
        // 取a
        Activity a = activityDao.getById(id);

        // 将uList 和a 打包到map
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);

        return map;
    }

    @Override
    public boolean update(Activity a) {

        boolean falg = true;

        int count = activityDao.update(a);
        if(count != 1) {
            falg = false;
        }
        return falg;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList =
                activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemark(id);

        if(count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityByNameAndNotByClueId(map);

        return aList;
    }

    @Override
    public List<Activity> getActivityByName(String aname) {

        List<Activity> aList = activityDao.getActivityByName(aname);

        return aList;
    }
}

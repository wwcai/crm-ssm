package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityByName(String aname);

    List<Activity> getActivityByNameAndNotByContactsId(Map<String, String> map);

    List<Activity> getActivityListByContactsId(String contactsId);
}

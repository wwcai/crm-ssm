package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.PrintJson;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.domain.ActivityRemark;
import com.wwcai.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private ActivityService as;

    @Resource
    private UserService us;

    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map<String, Object> updateRemark(HttpServletRequest request, ActivityRemark ar) {

        System.out.println("执行修改备注操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);

        boolean flag = as.updateRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        return map;
    }

    @RequestMapping(value = "/saveRemark")
    @ResponseBody
    public Map<String, Object> saveRemark(HttpServletRequest request, ActivityRemark ar) {

        System.out.println("执行添加备注的操作");
        String id = UUIDUtil.getUUID();
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        ar.setId(id);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);


        boolean flag = as.saveRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        return map;
    }

    @RequestMapping(value = "/deleteRemark.do")
    @ResponseBody
    public Map<String, Boolean> deleteRemark(String id) {

        System.out.println("删除备注操作");

        boolean flag = as.deleteRemark(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        System.out.println("根据市场活动ID，取得备注信息");

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        return arList;
    }

    @RequestMapping(value = "/detail.do")
    public String detail(HttpServletRequest request, String id){

        System.out.println("进入 跳转到详细信息页的操作");

        Activity a = as.detail(id);
        request.setAttribute("a", a);
        return "/workbench/activity/detail.jsp";
    }

    @RequestMapping(value = "/update.do")
    @ResponseBody
    public Map<String, Boolean> update(HttpServletRequest request, Activity a) {

        System.out.println("执行市场活动修改操作");

        // 修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人：当前登录人
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();

        a.setEditTime(editTime);
        a.setEditBy(editBy);


        boolean flag = as.update(a);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/getUserListAndActivity")
    @ResponseBody
    public Map<String, Object> getUserListAndActivity(String id) {

        System.out.println("进入查询用户信息列表和根据市场活动ID查询单条记录的操作");

        /*

            uList
            a
            用的不多  用map集合

         */

        Map<String, Object> map = as.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Map<String, Boolean> delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        String ids[] = request.getParameterValues("id");

        boolean flag = as.delete(ids);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> pageList(HttpServletRequest request, Activity a) {

        System.out.println("进入到查询市场活动信息列表的操作（条件查询 + 分页查询）");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出略过的记录数
        int skipCount = (pageNo - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("name", a.getName());
        map.put("owner", a.getOwner());
        map.put("startDate", a.getStartDate());
        map.put("endDate", a.getEndDate());
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        /*

            前端需要：市场活动信息列表和查询总条数

                业务层拿到以上信息，做返回
                map
                    map.put("datalist":datalist)
                    map.put("total":total)
                    PrintJSON map -->json
                    {"total":10, "datalist":[{市场活动1}.{2},{3}...]}

                将来分页查询 每个模块都有，使用通用的VO 方便
                vo
                    PaginationVO<T>
                        private int total;
                        private List<T> datalist;
                PaginationVO<Activity> vo = new PaginationVO<>;
                vo.setTotal(total);
                vo.setDatalist(datalist);
                PrintJSON vo -->json
                {"total":10, "datalist":[{市场活动1}.{2},{3}...]}

         */

        PaginationVo<Activity> vo = as.pageList(map);
        return vo;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String,Boolean> save(HttpServletRequest request, Activity a) {

        System.out.println("执行市场活动的更改");

        String id = UUIDUtil.getUUID();

        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();

        a.setId(id);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);


        boolean flag = as.save(a);
        Map<String,Boolean> map = new HashMap<String,Boolean>();
        map.put("success",flag);

        return map;

    }

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        List<User> uList = us.getUserList();
        return uList;
    }

}

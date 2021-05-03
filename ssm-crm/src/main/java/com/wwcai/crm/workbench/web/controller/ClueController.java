package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.domain.Clue;
import com.wwcai.crm.workbench.domain.ClueRemark;
import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.service.ActivityService;
import com.wwcai.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource
    private ClueService cs;

    @Resource
    private ActivityService as;

    @Resource
    private UserService us;


    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map<String, Object> updateRemark(HttpServletRequest request, ClueRemark cr) {

        System.out.println("执行修改备注操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        cr.setEditFlag(editFlag);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);

        boolean flag = cs.updateRemark(cr);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("cr", cr);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteRemark.do")
    public Map<String, Boolean> deleteRemark(String id) {

        System.out.println("删除备注信息");

        boolean flag = cs.deleteRemark(id);

        Map<String, Boolean> map = new HashMap<>();

        return map;
    }

    @RequestMapping(value = "/saveRemark.do")
    @ResponseBody
    public Map<String, Object> saveRemark(HttpServletRequest request, ClueRemark cr) {

        System.out.println("执行添加备注操作");

        String id = UUIDUtil.getUUID();
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        cr.setId(id);
        cr.setCreateBy(createBy);
        cr.setCreateTime(createTime);
        cr.setEditFlag(editFlag);

        boolean flag = cs.saveRemark(cr);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("cr", cr);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getRemarkListByCid")
    public List<ClueRemark> getRemarkListByCid(String clueId) {

        System.out.println("根据交易ID获取备注信息");

        List<ClueRemark> cList = cs.getRemarkListByCid(clueId);

        return cList;
    }

    @RequestMapping(value = "/convert.do")
    public String convert(HttpServletRequest request, Tran t) {

        System.out.println("执行线索转换操作");

        String clueId = request.getParameter("clueId");
        // 接受是否需要创建交易的标记
        String flag = request.getParameter("flag");

        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        // 如果需要创建标记
        if("a".equals(flag)) {

            // 接受需要创建交易的参数
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);

        }


        boolean flag1 = cs.convert(clueId, t, createBy);
        if(flag1) {
            return "redirect:/workbench/clue/index.jsp";
        }

        return "redirect:/error.jsp";
    }

    @RequestMapping(value = "/getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String aname) {

        System.out.println("查询市场活动列表，根据名称模糊查询");

        List<Activity> aList = as.getActivityByName(aname);
        return aList;
    }

    @RequestMapping(value = "/bund.do")
    @ResponseBody
    public Map<String, Boolean> bund(HttpServletRequest request, String cid) {

        System.out.println("执行关联市场活动操作");

        String[] aids = request.getParameterValues("aid");

        boolean flag = cs.bund(cid,aids);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getActivityByNameAndNotByClueId.do")
    public List<Activity> getActivityByNameAndNotByClueId(String aname, String clueId) {

        System.out.println("通过模糊查询市场活动列表（关联过的不查）");

        Map<String, String> map = new HashMap<>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        List<Activity> aList = as.getActivityByNameAndNotByClueId(map);

        return aList;

    }

    @RequestMapping(value = "/delete.do")
    public Map<String, Boolean> delete(HttpServletRequest request) {

        System.out.println("执行线索删除操作");

        String ids[] = request.getParameterValues("id");

        boolean flag = cs.delete(ids);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/upadte.do")
    @ResponseBody
    public Map<String, Boolean> update(HttpServletRequest request,Clue clue) {

        System.out.println("进入线索修改控制器");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User) request.getSession().getAttribute("user")).getName();

        clue.setEditBy(editBy);
        clue.setEditTime(editTime);

        boolean flag = cs.update(clue);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserListAndClue.do")
    public Map<String, Object> getUserListAndClue(String id) {

        System.out.println("进入查询用户信息列表和根据线索ID查询单条记录的操作");

        Map<String, Object> map = cs.getUserListAndClue(id);

        return map;
    }

    @RequestMapping(value = "/unbund.do")
    @ResponseBody
    public Map<String, Boolean> unbund(String id) {

        System.out.println("解除关联操作");

        boolean flag = cs.unbund(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId) {

        System.out.println("更具线索ID查询关联的市场活动列表");

        List<Activity> aList = as.getActivityListByClueId(clueId);

        return aList;
    }

    @RequestMapping(value = "/detail.do")
    public String detail(HttpServletRequest request, String id){

        System.out.println("跳转到线索的详细信息页");

        Clue c = cs.detail(id);
        request.setAttribute("c", c);
        return "/workbench/clue/detail.jsp";

    }

    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVo<Clue> pageList(Clue c, String pageNo, String pageSize) {

        System.out.println("进入线索信息列表");

        int pageNo1 = Integer.valueOf(pageNo);
        int pageSize1 = Integer.valueOf(pageSize);


        int skipCount = (pageNo1 - 1) * pageSize1;
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", c.getFullname());
        map.put("owner", c.getOwner());
        map.put("company", c.getCompany());
        map.put("phone", c.getPhone());
        map.put("mphone", c.getMphone());
        map.put("state", c.getState());
        map.put("source", c.getSource());
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize1);

        PaginationVo<Clue> vo = cs.pageList(map);

        return vo;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String, Boolean> save(HttpServletRequest request, Clue clue) {

        System.out.println("执行添加线索操作");

        String id = UUIDUtil.getUUID();

        String createTime = DateTimeUtil.getSysTime();
        String createBy =
                ((User) request.getSession().getAttribute("user")).getName();

        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);

        boolean flag = cs.save(clue);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserList.do")
    public List<User> getUserList() {

        System.out.println("取得用户信息列表");

        List<User> uList = us.getUserList();

        return uList;
    }


}

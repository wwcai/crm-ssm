package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.*;
import com.wwcai.crm.workbench.service.ActivityService;
import com.wwcai.crm.workbench.service.ContactsService;
import com.wwcai.crm.workbench.service.CustomerService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/contacts")
public class ContactsController {

    @Resource
    private ContactsService cs;
    @Resource
    private UserService us;
    @Resource
    private CustomerService cus;
    @Resource
    private ActivityService as;

    @ResponseBody
    @RequestMapping(value = "/pageList.do")
    public PaginationVo<Contacts> pageList(HttpServletRequest request, Contacts c) {

        System.out.println("进入客户信息列表");

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        String customerName = request.getParameter("customerName");

        int skipCount = (pageNo - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", c.getFullname());
        map.put("source", c.getSource());
        map.put("owner",c.getOwner());
        map.put("customerName", customerName);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        PaginationVo<Contacts> vo = cs.pageList(map);

        return vo;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String, Boolean> save(HttpServletRequest request, Contacts c) {

        System.out.println("保存客户信息");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String customerName = request.getParameter("customerName");
        System.out.println(customerName);
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();

        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setId(id);

        boolean flag = cs.save(c, customerName);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList() {

        System.out.println("获得用户信息列表");

        List<User> ulist = us.getUserList();

        return ulist;
    }

    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name) {

        System.out.println("取得客户名称列表（按照名称模糊查询）");

        List<String> slist = cus.getCustomerName(name);

        return slist;
    }

    @RequestMapping(value = "/detail.do")
    public String detail(HttpServletRequest request, String id) {

        System.out.println("跳转到联系人详细信息页");

        Contacts c = cs.detail(id);

        request.setAttribute("c", c);
        return "/workbench/contacts/detail.jsp";
    }

    @ResponseBody
    @RequestMapping(value = "/getUserListAndContacts.do")
    public Map<String, Object> getUserListAndContacts(String id) {

        System.out.println("取得用户和联系人信息");

        Map<String, Object> map = cs.getUserListAndContacts(id);

        return map;
    }

    @RequestMapping(value = "/update.do")
    @ResponseBody
    public Map<String, Boolean> update(HttpServletRequest request, Contacts c) {

        System.out.println("更新联系人信息");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String customerName = request.getParameter("customerName");

        c.setEditBy(editBy);
        c.setEditTime(editTime);

        boolean flag = cs.update(c, customerName);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Map<String, Boolean> delete(HttpServletRequest request) {

        System.out.println("删除联系人");

        String[] ids = request.getParameterValues("id");

        Boolean flag = cs.delete(ids);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getRemarkListByCid.do")
    public List<ContactsRemark> getRemarkListByCid(String contactsId) {
        System.out.println("进入根据contactsId查询备注信息");

        List<ContactsRemark> clist = cs.getRemarkListByCid(contactsId);

        return clist;
    }

    @RequestMapping(value = "/saveRemark.do")
    @ResponseBody
    public Map<String, Object> saveRemark(HttpServletRequest request, ContactsRemark cr) {

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
    @RequestMapping(value = "/deleteRemark.do")
    public Map<String, Boolean> deleteRemark(String id) {

        System.out.println("删除备注信息");

        boolean flag = cs.deleteRemark(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map<String, Object> updateRemark(HttpServletRequest request, ContactsRemark cr) {

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

    @RequestMapping(value = "/getTranListByContactsId.do")
    @ResponseBody
    public List<Tran> getTranListByContactsId(HttpServletRequest request, String contactsId) {

        System.out.println("根据客户id 查询交易信息");

        List<Tran> tlist = cs.getTranListByContactsId(contactsId);

        Map<String, String> pMap = (Map<String, String>) request.getSession().getServletContext().getAttribute("pMap");

        for (Tran t : tlist) {
            String possibility = pMap.get(t.getStage());
            t.setPossibility(possibility);
        }

        return tlist;
    }

    @ResponseBody
    @RequestMapping(value = "/getActivityByNameAndNotByContactsId.do")
    public List<Activity> getActivityByNameAndNotByContactsId(String aname, String contactsId) {

        System.out.println("通过模糊查询市场活动列表（关联过的不查）");

        Map<String, String> map = new HashMap<>();
        map.put("aname", aname);
        map.put("contactsId", contactsId);

        List<Activity> alist = as.getActivityByNameAndNotByContactsId(map);

        return alist;

    }

    @RequestMapping(value = "/getActivityListByContactsId.do")
    @ResponseBody
    public List<Activity> getActivityListByContactsId(String contactsId) {

        System.out.println("更具线索ID查询关联的市场活动列表");

        List<Activity> aList = as.getActivityListByContactsId(contactsId);

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

    @RequestMapping(value = "/unbund.do")
    @ResponseBody
    public Map<String, Boolean> unbund(String id) {

        System.out.println("解除关联操作");

        boolean flag = cs.unbund(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }
}

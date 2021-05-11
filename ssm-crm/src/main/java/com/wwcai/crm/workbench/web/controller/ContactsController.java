package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.domain.Customer;
import com.wwcai.crm.workbench.service.ContactsService;
import com.wwcai.crm.workbench.service.CustomerService;
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

}

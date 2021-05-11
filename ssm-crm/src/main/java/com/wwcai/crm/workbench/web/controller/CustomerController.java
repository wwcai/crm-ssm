package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Customer;
import com.wwcai.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/customer")
public class CustomerController {

    @Resource
    private CustomerService cs;
    @Resource
    private UserService us;


    @RequestMapping(value = "/detail.do")
    public String detail(HttpServletRequest request, String id) {

        System.out.println("跳转到顾客详细信息页");

        Customer c = cs.detail(id);

        request.setAttribute("c", c);
        return "/workbench/customer/detail.jsp";
    }

    @RequestMapping(value = "/update.do")
    @ResponseBody
    public Map<String, Boolean> update(HttpServletRequest request, Customer c) {

        System.out.println("更新客户信息");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();

        c.setEditBy(editBy);
        c.setEditTime(editTime);

        boolean flag = cs.update(c);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @RequestMapping(value = "/getUserListAndCustomer.do")
    @ResponseBody
    public Map<String, Object> getUserListAndCustomer(String id) {

        System.out.println("取得用户和客户信息");

        Map<String, Object> map = cs.getUserListAndCustomer(id);

        return map;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String, Boolean> save(HttpServletRequest request, Customer c) {

        System.out.println("保存客户信息");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();

        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setId(id);

        boolean flag = cs.save(c);

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

    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Map<String, Boolean> delete(HttpServletRequest request) {

        System.out.println("进入删除客户控制器");

        String[] ids = request.getParameterValues("id");

        boolean flag = cs.delete(ids);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVo<Customer> pageList(HttpServletRequest request, Customer c) {

        System.out.println("进入客户信息列表");

        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("name", c.getName());
        map.put("phone", c.getPhone());
        map.put("owner",c.getOwner());
        map.put("website", c.getWebsite());
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        PaginationVo<Customer> vo = cs.pageList(map);

        return vo;
    }
}

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
import com.wwcai.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

    @Resource
    private TranService ts;

    @Resource
    private CustomerService cs;

    @Resource
    private ContactsService css;

    @Resource
    private ActivityService as;

    @Resource
    private UserService us;



    @RequestMapping(value = "/getCharts.do")
    @ResponseBody
    public Map<String, Object> getCharts() {

        System.out.println("取得交易阶段数量统计图标的数据");

        Map<String, Object> map = ts.getChrats();

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/changeStage.do")
    public Map<String, Object> changeStage(HttpServletRequest request, Tran t) {

        System.out.println("改变交易阶段");

        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        t.setEditBy(editBy);
        t.setEditTime(editTime);

        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>) request.getSession().getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(t.getStage()));

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("t",t);

        return map;
    }

    @RequestMapping(value = "/getHistoryByTranId.do")
    @ResponseBody
    public List<TranHistory> getHistoryByTranId(HttpServletRequest request, String tranId) {

        System.out.println("通过交易ID查询历史列表");

        List<TranHistory> thlist = ts.getHistoryByTranId(tranId);

        Map<String, String> pMap = (Map<String, String>) request.getSession().getServletContext().getAttribute("pMap");

        for(TranHistory th : thlist) {

            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }

        return thlist;
    }

    @RequestMapping(value = "/detail.do")
    public String detail(HttpServletRequest request, String id) {

        System.out.println("跳转到详细信息页");

        Tran t = ts.detail(id);

        String stage = t.getStage();
        Map<String, String> pMap = (Map<String, String>) request.getSession().getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        t.setPossibility(possibility);
        request.setAttribute("t", t);

        return "/workbench/transaction/detail.jsp";
    }

    @RequestMapping(value = "/getUserListAndTran.do")
    @ResponseBody
    public Map<String, Object> getUserListAndTran(String id) {

        System.out.println("进入查询用户信息列表和根据线索ID查询单条记录的操作");

        Map<String, Object> map = ts.getUserListAndTran(id);

        return map;
    }

    @RequestMapping(value = "/update.do")
    public String update(HttpServletRequest request, Tran t) {

        System.out.println("执行更新交易操作");

        String customerName = request.getParameter("customerName");
        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();

        t.setEditBy(editBy);
        t.setEditTime(editTime);


        boolean flag = ts.update(t,customerName);

        if(flag) {

            return "redirect:/workbench/transaction/index.jsp";

        }

        return "redirect:error.jsp";

    }

    @RequestMapping(value = "/save.do")
    public String save(HttpServletRequest request, Tran t) {

        System.out.println("执行添加交易操作");

        String customerName = request.getParameter("customerName");
        Boolean flag = Boolean.valueOf(request.getParameter("flag"));
        Boolean flag2 = Boolean.valueOf(request.getParameter("flag2"));
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();

        t.setId(id);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);

        System.out.println("=====" + flag);
        System.out.println("=====" + flag2);


        boolean flag1 = ts.save(t,customerName);

        if (!flag)
            return "redirect:/workbench/customer/detail.do?id=" + t.getCustomerId();

        if (!flag2)
            return "redirect:/workbench/contacts/detail.do?id=" + t.getContactsId();

        if (flag1) {

            return "redirect:/workbench/transaction/index.jsp";

        }

        return "redirect:error.jsp";

    }

    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name) {

        System.out.println("取得客户名称列表（按照名称模糊查询）");

        List<String> slist = cs.getCustomerName(name);

        return slist;
    }

    @RequestMapping(value = "/getContactsByName.do")
    @ResponseBody
    public List<Contacts> getContactsByName(String cname) {

        System.out.println("通过名称模糊查询联系人名称");

        List<Contacts> clist = css.getContactsByName(cname);

       return clist;
    }

    @RequestMapping(value = "/getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String aname) {

        System.out.println("通过名称模糊查询市场活动");

        List<Activity> alist = as.getActivityByName(aname);

        return alist;
    }

    @RequestMapping(value = "/add.do")
    public String add(HttpServletRequest request) {

        System.out.println("进入跳转到交易添加页的操作");

        String customerName = request.getParameter("customerName");
        String contactsName = request.getParameter("contactsName");
        String contactsId = request.getParameter("contactsId");
        List<User> ulist = us.getUserList();
        Boolean flag = true;
        Boolean flag2 = true;
        request.setAttribute("ulist", ulist);

        if (!"b".equals(customerName) && customerName != null) {
            flag = false;
            request.setAttribute("customerName", customerName);
        }

        if (!"c".equals(contactsName)) {
            flag2 = false;
            request.setAttribute("contactsName", contactsName);
            request.setAttribute("contactsId", contactsId);
        }

        request.setAttribute("flag", flag);
        request.setAttribute("flag2", flag2);
        return "/workbench/transaction/save.jsp";
    }

    @RequestMapping(value = "/pageList.do")
    @ResponseBody
    public PaginationVo<Tran> pageList(HttpServletRequest request, Tran t, String pageNo, String pageSize) {

        System.out.println("进入交易信息列表");

        String customerName = request.getParameter("customerName");
        String contactsName = request.getParameter("contactsName");
        int pageNo1 = Integer.valueOf(pageNo);
        int pageSize1 = Integer.valueOf(pageSize);


        int skipCount = (pageNo1 - 1) * pageSize1;
        Map<String, Object> map = new HashMap<>();
        map.put("name", t.getName());
        map.put("owner", t.getOwner());
        map.put("type", t.getType());
        map.put("stage", t.getStage());
        map.put("source", t.getSource());
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize1);

        PaginationVo<Tran> vo = ts.pageList(map, customerName, contactsName);

        return vo;
    }

    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public Map<String, Object> updateRemark(HttpServletRequest request, TranRemark tr) {

        System.out.println("执行修改备注操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        tr.setEditFlag(editFlag);
        tr.setEditBy(editBy);
        tr.setEditTime(editTime);

        boolean flag = ts.updateRemark(tr);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("tr", tr);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteRemark.do")
    public Map<String, Boolean> deleteRemark(String id) {

        System.out.println("删除备注信息");

        boolean flag = ts.deleteRemark(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping(value = "/saveRemark.do")
    @ResponseBody
    public Map<String, Object> saveRemark(HttpServletRequest request, TranRemark tr) {

        System.out.println("执行添加备注操作");

        String id = UUIDUtil.getUUID();
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        tr.setId(id);
        tr.setCreateBy(createBy);
        tr.setCreateTime(createTime);
        tr.setEditFlag(editFlag);

        boolean flag = ts.saveRemark(tr);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("tr", tr);

        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/getRemarkListByTid")
    public List<TranRemark> getRemarkListByTid(String tranId) {

        System.out.println("根据交易ID获取备注信息");

        List<TranRemark> tList = ts.getRemarkListByTid(tranId);

        return tList;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Map<String, Boolean> delete(HttpServletRequest request) {

        String[] ids = request.getParameterValues("id");

        Boolean flag = ts.delete(ids);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        return map;

    }

}

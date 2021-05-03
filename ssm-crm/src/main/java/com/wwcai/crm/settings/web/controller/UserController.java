package com.wwcai.crm.settings.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.MD5Util;
import com.wwcai.crm.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController  {

    @Resource
    private UserService us;

    @RequestMapping(value = "settings/user/login.do")
    public void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        // 将密码的明文形式装换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接受浏览器端的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("ip = " + ip);

        // 未来业务层开发，统一使用代理类形态的接口对象
        //UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = us.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user", user);
            // 如果执行到此处，说明业务层 没有为controller抛出异常 登录成功
            // 为前端提供{"success" ： true}
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            e.printStackTrace();

            // 一旦程序执行了catch块，表示业务层为controller抛出了异常 登录失败
            // 为前端提供{"success" ： false, "msg" : ?}
            String msg = e.getMessage();

            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }

    }

}

package com.wwcai.crm.settings.service.impl;

import com.wwcai.crm.exception.LoginException;
import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if (user == null) {
            throw new LoginException("您的账号或密码错误");
        }

        // 能执行到此处，说明账号密码正确，
        // 需要继续向下验证其他三项信息

        // 判断IP地址
        /*if(!user.getAllowIps().contains(ip)) {
            throw new LoginException("您的ip地址受限");
        }*/

        // 验证失效时间
        if(user.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0) {
            throw new LoginException("您的账号已失效");
        }

        // 判断锁定状态
        if("0".equals(user.getLockState())) {
            throw new LoginException("您的账号已锁定");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }
}

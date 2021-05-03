package com.wwcai.crm.settings.service;

import com.wwcai.crm.exception.LoginException;
import com.wwcai.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}

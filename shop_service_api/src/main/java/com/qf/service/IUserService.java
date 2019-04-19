package com.qf.service;

import com.qf.entity.User;

public interface IUserService {

    //添加用户
    int insertUser(User user);
    //登录中户
    User loginUser(String username,String password);
    //激活用户
    int activateUser(User user);
}

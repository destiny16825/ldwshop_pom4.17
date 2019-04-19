package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public int insertUser(User user) {
        //加密密码
        user.setPassword(Md5Utils.md5(user.getPassword()));
        return userMapper.insert(user);
    }

    /**
     * 登录用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User loginUser(String username, String password) {
        return null;
    }

    /**
     * 激活用户
     * @param user
     * @return
     */
    @Override
    public int activateUser(User user) {
        return 0;
    }
}

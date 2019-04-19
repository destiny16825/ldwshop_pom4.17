package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sso")
public class SsoController {

    @Reference
    private IUserService userService;

    /**
     * 跳转注册的方法
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    /**
     * 跳转登录的方法
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 注册的方法
     * @return
     */
    @RequestMapping("/register")
    public String register(User user, ModelMap map){
        int result= userService.insertUser(user);
        if (result<=0){
            //注册失败
            map.put("error", "0");
            return "register";
        }
        return "login";
    }

}

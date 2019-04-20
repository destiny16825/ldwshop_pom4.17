package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sso")
public class SsoController {

    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

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

    /**
     * 登录的页面
     * @return
     */
    @RequestMapping("/login")
    public String login(String username, String password, ModelMap map, HttpServletResponse response){
        User user = userService.loginUser(username, password);
        //登录失败，返回到登录页面
        if (user==null){
            System.out.println("登录失败");
            map.put("erro","0");
            return "login";
        }
        //把用户信息存放到redis服务器中,设置缓存的时间
        String token= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,user);
        redisTemplate.expire(token,10, TimeUnit.DAYS);

        //将用户令牌设置到cookie中
        Cookie cookie=new Cookie("login_token",token);
        cookie.setMaxAge(60*60*24*10);//时长
        cookie.setPath("/");
        response.addCookie(cookie);


        String returnUrl="http://localhost:8081";
        //登录成功
        return "redirect:"+returnUrl;
    }

    @RequestMapping("/isLogin")
    @ResponseBody
    public String isLogin(@CookieValue(name = "login_token",required = false) String loginToken){
        //获取浏览器cookie中的login_token
        System.out.println("cookie中的登录令牌："+loginToken);
        //从redis缓存中取得user对象
        User user=null;
        if (loginToken!=null){
           user=(User) redisTemplate.opsForValue().get(loginToken);
        }
        //将对象转化为json字符串返回

        return  user==null?"ifLogin(null)":"ifLogin(' "+ JSON.toJSONString(user) +" ')";
    }

}

package com.qf.shop_item;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopItemApplicationTests {

    @Autowired
    //配置对象
    private Configuration configuration;

    @Test
    public void contextLoads() throws Exception {

        //4指定路径
        String outPath="C:\\Users\\Administrator.USER-20180919NT\\Desktop\\hello.html";
        //准备输出流
        Writer writer=new FileWriter(outPath);
        Goods goods=new Goods(1,"华为手机",null,100,"dsaf","hkd",1,new Date(),1);
        List<Goods> goodsList=new ArrayList<>();
        goodsList.add(goods);
        //1通过配置对象获取模板对象
        Template template = configuration.getTemplate("hello.ftl");
        //2准备数据
        Map<String,Object> data=new HashMap<>();
        data.put("key",goods);
        data.put("age",100);
        data.put("goodList",goodsList);
        //3.将模板和数据静态化整合
        template.process(data,writer);
       //关闭输出流
        writer.close();

    }

}

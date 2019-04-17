package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private Configuration configuration;

    /**
     * 通过商品id生成静态页面的方法
     * @param goodId
     * @return
     */
    @RequestMapping("/createHtml")
    private String createHtml(int goodId , HttpServletRequest request){
        Goods goods=goodsService.queryById(goodId);
        String goodImage=goods.getGoodImage();
        String[] images=goodImage.split("\\|");
        //通过模板生成静态页面
        try {
            //1获得商品的模板对象
            Template template = configuration.getTemplate("goodsItem.ftl");
            //2准备数据
            Map<String,Object> map =new HashMap<>();
            map.put("goods",goods);
            map.put("images",images);
            //把基础路径传到前端页面
            map.put("context",request.getContextPath());

            //生成静态页面,获得classpath路径
            //静态页的名称必须和商品有所关联，最简单的做法就是用商品id作为页面的名字
            String path=this.getClass().getResource("/static/page/").getPath()+ goods.getId() + ".html";
            template.process(map,new FileWriter(path));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private IGoodsService goodsService;

    @Value("${server.ip}")
    private String serverIp;
    /**
     * 商品列表
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(ModelMap map){
        List<Goods> goods = goodsService.queryAll();
        System.out.println("商品列表："+goods);
        map.put("goods",goods);
        map.put("serverIp",serverIp);
        return "goodsList";
    }
    @RequestMapping("/insert")
    public String insert(Goods goods){
        System.out.println("添加商品的信息：" + goods);
        goodsService.insert(goods);
        return "redirect:/goods/list";
    }
}

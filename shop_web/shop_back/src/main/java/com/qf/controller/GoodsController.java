package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private IGoodsService goodsService;

    /**
     * 商品列表
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(ModelMap map){
        List<Goods> goods = goodsService.queryAll();
        System.out.println("商品列表："+goods);
        map.put("goods",goods);
        return "goodList";
    }
}

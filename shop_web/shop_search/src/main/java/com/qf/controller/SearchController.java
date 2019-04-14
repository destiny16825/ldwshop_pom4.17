package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISerachService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISerachService serachService;

    //根据关键字查询solr库
    @RequestMapping("searchByKeyWord")
    public String searchByKeyWord(String keyword, ModelMap map){
        System.out.println("获得搜索工程的关键字:"+keyword);
        List<Goods> goodsList = serachService.searchGoods(keyword);
        System.out.println("获得商品的集合："+goodsList);
        map.put("goodList",goodsList);
        return "searchList";
    }
}

package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISerachService {

    //根据关键字查询商品列表
    List<Goods> searchGoods(String keyword);

    //将商品同步到索引库solr
    int insertGoods(Goods goods);
}

package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISerachService;
import com.qf.shop_service_goods.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    //注入rabbitmq模板对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //通过dubbo注入ISerachService对象
    @Reference
    private ISerachService serachService;

    /**
     * 查询商品集合
     * @return
     */
    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectList(null
        );
    }

    /**
     * 添加商品
     * @param goods
     * @return
     */
    @Override
    public int insert(Goods goods) {
        //1将商品添加到数据库
        int result= goodsMapper.insert(goods);
        /*//2通过dubbo调用搜索服务，将商品同步到solr索引库
        serachService.insertGoods(goods);*/

        //2将添加用户信息放入到消息中间件
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.FANOUT_NAME,"",goods);


        return result;
    }

    @Override
    public Goods queryById(int goodId) {
        return goodsMapper.selectById(goodId);
    }
}

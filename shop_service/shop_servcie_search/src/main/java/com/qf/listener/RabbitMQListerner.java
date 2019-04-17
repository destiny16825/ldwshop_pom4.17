package com.qf.listener;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListerner {

    @Autowired
    private SolrClient solrClient;

    @RabbitListener(queues = "goods_queue1")
    public void handleMsg(Goods goods){
        System.out.println("接收rabitmq的信息"+goods);
        //同步到索引库
        SolrInputDocument solrDocument=new SolrInputDocument();
        solrDocument.setField("id",goods.getId());
        solrDocument.setField("goodName",goods.getGoodName());
        solrDocument.setField("goodInfo",goods.getGoodInfo());
        solrDocument.setField("goodPrice",goods.getGoodPrice().doubleValue());
        solrDocument.setField("goodImage",goods.getGoodImage());
        solrDocument.setField("goodSave",goods.getGoodSave());

        try {
            solrClient.add(solrDocument);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.qf.shop_search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {
    @Autowired
    private SolrClient solrClient;

    @Reference
    private IGoodsService goodsService;

    @Test
    public void add(){
        SolrInputDocument solrInputFields=new SolrInputDocument();
        solrInputFields.addField("id","10");
        solrInputFields.addField("goodName","苹果手机");
        solrInputFields.addField("goodInfo","昂贵的奢侈品");
        solrInputFields.addField("goodImage","www.baidu.com");
        solrInputFields.addField("goodSave","111");
        solrInputFields.addField("goodPrice","123.00");

        try {
            solrClient.add(solrInputFields);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() throws IOException, SolrServerException {
       /* List<Goods> goodsList = goodsService.queryAll();
        for (Goods goods : goodsList) {
            solrClient.deleteById(goods.getId()+"");
            solrClient.commit();
        }*/
        //solrClient.deleteByQuery("goodName:华为手机");
        solrClient.deleteById("31");
        solrClient.commit();
    }

    @Test
    public void query() throws IOException, SolrServerException {
        SolrQuery solrQuery=new SolrQuery();
        String keyWord="手机";
        solrQuery.setQuery("goodName:"+keyWord+ "|| goodInfo:" + keyWord);
        QueryResponse response = solrClient.query(solrQuery);

        SolrDocumentList results = response.getResults();
        for (SolrDocument document: results) {
            String id = (String) document.get("id");
            String goodName = (String) document.get("goodName");
            String goodInfo = (String) document.get("goodInfo");
            Float goodPrice = (Float) document.get("goodPrice");
            int goodSave = (int) document.get("goodSave");
            String goodImage = (String) document.get("goodImage");

            System.out.println(id +"，"+goodName+","+goodInfo+","+goodPrice+","+goodSave+","+goodImage);
        }
    }

    /**
     * 将数据库同步到solr索引库
     */
    @Test
    public void insertByMysql(){
        List<Goods> goodsList = goodsService.queryAll();
        for (Goods goods : goodsList) {
            SolrInputDocument solrInputFields=new SolrInputDocument();
            solrInputFields.addField("id",goods.getId());
            solrInputFields.addField("goodName",goods.getGoodName());
            solrInputFields.addField("goodInfo",goods.getGoodInfo());
            solrInputFields.addField("goodImage",goods.getGoodImage());
            solrInputFields.addField("goodSave",goods.getGoodSave());
            solrInputFields.addField("goodPrice",goods.getGoodPrice());
            try {
                solrClient.add(solrInputFields);
                solrClient.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void contextLoads() {
    }

}

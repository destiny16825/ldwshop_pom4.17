package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISerachService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISerachService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> searchGoods(String keyword) {
        System.out.println("搜索工程调用了搜索服务并获得关键字："+keyword);
        //1创建一个solrquery对象，并设置查询对象的值
        SolrQuery solrQuery=new SolrQuery();
        if (keyword==null){
            solrQuery.setQuery("*:*");//查询所有
        }else {
            solrQuery.setQuery("goodName:"+keyword+" || goodInfo:"+keyword);
        }

        //开启高亮，设置前后缀
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //添加需要高亮的字段
        solrQuery.addHighlightField("goodName");

        List<Goods> goodsList=new ArrayList<>();
        try {
            //2通过查询对象获得查询的结果集
            QueryResponse result = solrClient.query(solrQuery);

            //获得高亮的集合
            //Map<id, Map<Feild, List<String>>
            //id - 高亮的商品id;//Field - 高亮的字段//List<String>:高亮的内容
            Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();

            SolrDocumentList solrDocumentList = result.getResults();
            //3遍历得到商品对象
            for( SolrDocument solrDocument :solrDocumentList
                 ) {
                //4.创建一个商品对象，并把得到的值设置到对象中
                Goods goods=new Goods();
                goods.setId(Integer.parseInt(solrDocument.get("id")+""));
                goods.setGoodName(solrDocument.get("goodName")+"");
                goods.setGoodInfo(solrDocument.get("goodInfo")+"");
                goods.setGoodPrice(BigDecimal.valueOf(Double.parseDouble(solrDocument.get("goodPrice") + "")));
                goods.setGoodImage(solrDocument.get("goodImage")+"");
                goods.setGoodSave(Integer.parseInt(solrDocument.get("goodSave")+""));
                goodsList.add(goods);

                //判断是否有高亮内容(高亮集合的第一个参数是id,所有直接判断是否包含商品的id)
                System.out.println("highlighting："+highlighting);

                //有高亮内容集合
                System.out.println("get后的值："+highlighting.get(goods.getId() + ""));
                Map<String, List<String>> goodsHightlist = highlighting.get(goods.getId() + "");
                System.out.println("list里的值："+goodsHightlist.get("goodName"));
                if(goodsHightlist.get("goodName")!=null){
                    //获得高亮的内容（goodName）
                    String goodName = goodsHightlist.get("goodName").get(0);
                    //将高亮内容替换到goods对象中
                    goods.setGoodName(goodName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    @Override
    public int insertGoods(Goods goods) {
        //将商品添加到索引库
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
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

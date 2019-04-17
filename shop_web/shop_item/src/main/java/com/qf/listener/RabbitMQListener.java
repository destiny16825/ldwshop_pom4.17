package com.qf.listener;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQListener {

    @Autowired
    private Configuration configuration;

    @RabbitListener(queues = "goods_queue2")
    public void handleRabbitMsg(Goods goods){
        System.out.println("搜索工程获取RabbitMq的消息："+goods);

        //生成静态页面
        String goodImage=goods.getGoodImage();
        String[] images=goodImage.split("\\|");
        //通过模板生成静态页面
        try {
            //1获得商品的模板对象
            Template template = configuration.getTemplate("goodsItem.ftl");
            //获得request对象
           /* RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();*/

            //2准备数据
            Map<String,Object> map =new HashMap<>();
            map.put("goods",goods);
            map.put("images",images);
            //把基础路径传到前端页面
            //map.put("context",request.getContextPath());

            //生成静态页面,获得classpath路径
            //静态页的名称必须和商品有所关联，最简单的做法就是用商品id作为页面的名字
            String path=this.getClass().getResource("/static/page/").getPath()+ goods.getId() + ".html";
            template.process(map,new FileWriter(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

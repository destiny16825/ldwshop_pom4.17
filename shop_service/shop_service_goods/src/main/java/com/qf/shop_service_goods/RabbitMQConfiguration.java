package com.qf.shop_service_goods;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String FANOUT_NAME="goods_fanoutExchange";

    //创建队列1
    @Bean
    public Queue getQueue1(){
        return new Queue("goods_queue1");
    }

    //创建队列2
    @Bean
    public Queue getQueue2(){
        return new Queue("goods_queue2");
    }

    //创建交换机
    @Bean
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange(FANOUT_NAME);
    }

    //绑定队列1
    @Bean
    public Binding getBindingBuilder1(Queue getQueue1, FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue1).to(getFanoutExchange);
    }

    //绑定队列2
    @Bean
    public Binding getBindingBuilder2(Queue getQueue2, FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue2).to(getFanoutExchange);
    }
}

package com.qf.shop_servcie_search;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
@DubboComponentScan("com.qf.serviceimpl")
public class ShopServcieSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServcieSearchApplication.class, args);
    }

}

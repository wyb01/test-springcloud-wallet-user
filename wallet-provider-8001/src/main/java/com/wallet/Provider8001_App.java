package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 19:19
 **/
@SpringBootApplication
@EnableEurekaClient   //启动新组件技术,eureka客户端,会自动注册进eureka
@EnableDiscoveryClient
public class Provider8001_App {

    public static void main(String[] args) {
        SpringApplication.run(Provider8001_App.class,args);
    }
}

package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: wallet
 * @description: eureka-7001主启动类
 * @author: wyb
 * @create: 2019-09-18 10:53
 **/
@SpringBootApplication
@EnableEurekaServer                    //开启eureka组件的标签
public class EurekaServer7001_App {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer7001_App.class,args);
    }
}

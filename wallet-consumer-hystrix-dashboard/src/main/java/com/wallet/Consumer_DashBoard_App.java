package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-18 18:39
 **/
@SpringBootApplication
@EnableHystrixDashboard   //开启仪表盘监控注解
public class Consumer_DashBoard_App {
    public static void main(String[] args) {
        SpringApplication.run(Consumer_DashBoard_App.class,args);
    }
}

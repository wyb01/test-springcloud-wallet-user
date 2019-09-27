package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 19:19
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableCircuitBreaker  //对Hystrix熔断机制的支持
public class Provider8001_Hystrix_App {

    public static void main(String[] args) {
        SpringApplication.run(Provider8001_Hystrix_App.class,args);
    }
}

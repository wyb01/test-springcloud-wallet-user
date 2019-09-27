package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 20:50
 **/
@SpringBootApplication
@EnableEurekaClient
public class Consumer80_App {

    public static void main(String[] args) {
        SpringApplication.run(Consumer80_App.class,args);
    }
}

package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-18 19:07
 **/
@SpringBootApplication
@EnableZuulProxy
public class Zuul9527_StartSpringCloud_App {
    public static void main(String[] args) {
        SpringApplication.run(Zuul9527_StartSpringCloud_App.class,args);
    }
}

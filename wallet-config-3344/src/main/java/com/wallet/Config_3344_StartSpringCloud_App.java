package com.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-18 20:11
 **/
@SpringBootApplication
@EnableConfigServer
public class Config_3344_StartSpringCloud_App {
    public static void main(String[] args) {
        SpringApplication.run(Config_3344_StartSpringCloud_App.class,args);
    }
}

package com.wallet.cfgbean;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: wallet
 * @description: 注解版的配置
 * @author: wyb
 * @create: 2019-09-17 20:38
 **/
@Configuration
public class ConfigBean {    //boot --> 等同于spring applicationContext.xml ---

    // applicationContext.xml == ConfigBean(@Configuration)    boot推荐用注解
    // <bean id = "userServiceImpl" class = "com.wallet.service.userServiceImpl">

    @Bean
    @LoadBalanced   //Spring Cloud Ribbon是基于Netflix Ribbon实现的一套"客户端"负载均衡的工具
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /** 
    * @Description: 自定义Ribbon算法
    * @Param: [] 
    * @return: com.netflix.loadbalancer.IRule 
    * @Author: wyb
    * @Date: 2019/9/18 15:30
    */
    @Bean
    public IRule myRule() {
        //return new RoundRobinRule();  //"轮询算法"
        return new RandomRule();        //达到的目的，用我们重新选择的"随机算法"替代默认的轮询
    }
}

package com.wallet.controller;

import com.wallet.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 20:44
 **/
@RestController
public class ConsumerController {

    private static final String REST_URL_PREFIX = "http://WALLET-PROVIDER"; //微服务的真实名称

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/user/add")
    public boolean add(User user)
    {
        return restTemplate.postForObject(REST_URL_PREFIX + "/user/add", user, Boolean.class);
    }

    @RequestMapping(value = "/consumer/user/get/{id}")
    public User get(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/get/" + id, User.class);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/user/list")
    public List<User> list()
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/list", List.class);
    }

    // 测试@EnableDiscoveryClient,消费端可以调用服务发现
    @RequestMapping(value = "/consumer/user/discovery")
    public Object discovery()
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/discovery", Object.class);
    }

}

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
 * @description: 消费者控制器
 * @author: wyb
 * @create: 2019-09-17 20:44
 **/
@RestController
public class ConsumerController {

    private static final String REST_URL_PREFIX = "http://WALLET-PROVIDER"; //微服务的真实名称

    @Autowired
    private RestTemplate restTemplate;  //spring提供的客户端模板工具集 --- 发送rest请求，访问restful接口,
                                        // （url,requestMap,ResponseBean.class这三个参数分别代表Rest请求地址、请求参数、HTTP响应转换成的对象类型

    /** 
    * @Description: 客户端访问服务端添加用户接口
    * @Param: [user] 
    * @return: boolean 
    * @Author: wyb
    * @Date: 2019/9/27 19:51
    */
    @RequestMapping(value = "/consumer/user/add")
    public boolean add(User user)
    {
        return restTemplate.postForObject(REST_URL_PREFIX + "/user/add", user, Boolean.class);
    }

    /**
    * @Description: 客户端访问服务端获取用户信息接口
    * @Param: [id]
    * @return: com.wallet.api.entity.User
    * @Author: wyb
    * @Date: 2019/9/27 19:52
    */
    @RequestMapping(value = "/consumer/user/get/{id}")
    public User get(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/get/" + id, User.class);
    }

    /**
    * @Description:获取用户列表
    * @Param: []
    * @return: java.util.List<com.wallet.api.entity.User>
    * @Author: wyb
    * @Date: 2019/9/27 19:54
    */
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

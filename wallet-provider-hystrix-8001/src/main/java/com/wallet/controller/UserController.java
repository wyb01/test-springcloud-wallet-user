package com.wallet.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wallet.api.entity.User;
import com.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 18:48
 **/
@RestController
public class UserController {

    @Autowired
    private UserService service = null;

    @RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
    //一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法（服务熔断）
    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    public User get(@PathVariable("id") Long id)
    {

        User user = this.service.get(id);

        if (null == user) {
            throw new RuntimeException("该ID：" + id + "没有对应的信息");  //抛出了错误信息，自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
        }

        return user;
    }
    public User processHystrix_Get(@PathVariable("id") Long id)
    {
        return new User().setUserId(id).setNickName("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand")
                .setDb_source("no this database in MySQL");
    }


}

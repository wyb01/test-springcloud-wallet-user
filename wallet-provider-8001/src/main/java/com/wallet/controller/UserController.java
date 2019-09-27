package com.wallet.controller;

import com.wallet.api.entity.User;
import com.wallet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 18:48
 **/
@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private DiscoveryClient client;  //服务发现

    @ApiOperation(value = "添加用户",notes = "添加用户")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "user",value = "用户详情实体类")
    })
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public boolean add(@RequestBody User user)
    {
        return service.add(user);
    }

    @ApiOperation(value = "获取用户",notes = "根据用户id获取用户信息")
    @ApiImplicitParams({
   	@ApiImplicitParam(name = "id",value = "")
    })
    @RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") Long id)
    {
        return service.get(id);
    }

    @ApiOperation(value = "用户全查询",notes = "查询全部用户")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "null",value = "")
    })
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List<User> list()
    {
        return service.list();
    }


    @RequestMapping(value = "/user/discovery", method = RequestMethod.GET)
    public Object discovery()
    {
        List<String> list = client.getServices();
        System.out.println("**********" + list);

        List<ServiceInstance> srvList = client.getInstances("WALLET-PROVIDER");
        for (ServiceInstance element : srvList) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.client;
    }

}

package com.wallet.api.service;

import com.wallet.api.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

/**
 * @Description: 修改wallet-api工程，根据已经有的UserClientService接口新建一个实现了FallbackFactory接口的类UserClientServiceFallbackFactory
 * @author
 * @date
 */
//@FeignClient(value="WALLET-PROVIDER")   //对微服务进行面向"接口"的编程
@FeignClient(value="WALLET-PROVIDER",fallbackFactory = UserClientServiceFallbackFactory.class)   //1、对微服务进行面向"接口"的编程 2、服务降级
public interface UserClientService {

    @RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") long id);

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List<User> list();

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public boolean add(User user);

}

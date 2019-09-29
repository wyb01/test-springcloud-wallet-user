package com.wallet.api.service;

import com.wallet.api.entity.User;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: wallet
 * @description: 服务降级类，实现FallbackFactory接口
 * @author: wyb
 * @create: 2019-09-18 17:30
 **/
@Component
public class UserClientServiceFallbackFactory implements FallbackFactory<UserClientService> {

    /**
    * @Description:  UserClientService的熔断都放入UserClientServiceFallbackFactory中，与controller解耦
    * @Param: [throwable]
    * @return: com.wallet.api.service.UserClientService
    * @Author: wyb
    * @Date: 2019/9/29 11:03
    */
    @Override
    public UserClientService create(Throwable throwable) {

        return new UserClientService() {
            @Override
            public User get(long id) {
                return new User().setUserId(id).setNickName("该ID：" + id + "没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
                        .setDb_source("no this database in MySQL");
            }

            @Override
            public List<User> list() {
                return null;
            }

            @Override
            public boolean add(User user) {
                return false;
            }
        };
    }

}

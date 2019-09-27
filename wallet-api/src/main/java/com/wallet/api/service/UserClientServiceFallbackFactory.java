package com.wallet.api.service;

import com.wallet.api.entity.User;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-18 17:30
 **/
@Component
public class UserClientServiceFallbackFactory implements FallbackFactory<UserClientService> {

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

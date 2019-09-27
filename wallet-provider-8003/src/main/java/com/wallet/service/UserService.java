package com.wallet.service;

import com.wallet.api.entity.User;

import java.util.List;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 18:34
 **/
public interface UserService {

    public boolean add(User user);

    public User get(Long id);

    public List<User> list();

}

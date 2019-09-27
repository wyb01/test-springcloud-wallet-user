package com.wallet.service.impl;


import com.wallet.api.entity.User;
import com.wallet.dao.UserDao;
import com.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wallet
 * @description:
 * @author: wyb
 * @create: 2019-09-17 18:38
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Override
    public boolean add(User user) {
        return dao.addUser(user);
    }

    @Override
    public User get(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<User> list() {
        return dao.findAll();
    }
}

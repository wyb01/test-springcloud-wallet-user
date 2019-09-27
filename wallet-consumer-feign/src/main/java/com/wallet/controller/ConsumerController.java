package com.wallet.controller;

import java.util.List;

import com.wallet.api.entity.User;
import com.wallet.api.service.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ConsumerController
{
	@Autowired
	private UserClientService service;

	@RequestMapping(value = "/consumer/user/get/{id}")
	public User get(@PathVariable("id") Long id)
	{
		return this.service.get(id);
	}

	@RequestMapping(value = "/consumer/user/list")
	public List<User> list()
	{
		return this.service.list();
	}

	@RequestMapping(value = "/consumer/user/add")
	public Object add(User user)
	{
		return this.service.add(user);
	}
}

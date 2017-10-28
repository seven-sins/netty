package com.lonecpp.server.controller;

import org.springframework.stereotype.Controller;

import com.lonecpp.core.po.User;
import com.lonecpp.server.annotation.Action;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:12:09
 */
@Controller
public class UserController {

	@Action("userSave")
	public Object save(User user){
		System.out.println(user.getName());
		return user;
	}
}

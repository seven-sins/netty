package com.lonecpp.server.controller;

import org.springframework.stereotype.Controller;

import com.lonecpp.core.base.BaseController;
import com.lonecpp.core.common.Status;
import com.lonecpp.core.pb.UserProbuf.PbUser;
import com.lonecpp.core.po.User;
import com.lonecpp.core.vo.Result;
import com.lonecpp.server.annotation.Action;

/**
 * @author seven sins
 * @date 2017年10月28日 上午1:12:09
 */
@Controller
public class UserController extends BaseController {

	@Action("userSave")
	public Result save(User user){
		System.out.println(user.getName());
		return new Result(Status.SUCCESS, user);
	}
	
	@Action("userAdd")
	public Object add(PbUser pbUser){
		return response(Status.SUCCESS, "操作成功");
	}
}

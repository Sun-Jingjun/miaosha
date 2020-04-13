package com.jingjun.controller;

import com.jingjun.domain.User;
import com.jingjun.rabbitmq.MQReceiver;
import com.jingjun.rabbitmq.MQSender;
import com.jingjun.redis.RedisService;
import com.jingjun.result.CodeMsg;
import com.jingjun.result.Result;
import com.jingjun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private RedisService redisService;

	@Autowired
	private MQSender mqSender;

	@Autowired
	private MQReceiver mqReceiver;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}
	//1.rest api json输出 2.页面
	@RequestMapping("/hello")
	@ResponseBody
	public Result<String> hello() {
		return Result.success("hello,imooc");
	   // return new Result(0, "success", "hello,imooc");
	}

	@RequestMapping("/helloError")
	@ResponseBody
	public Result<String> helloError() {
		return Result.error(CodeMsg.SERVER_ERROR);
		//return new Result(500102, "XXX");
	}

	@RequestMapping("/thymeleaf")
	public String  thymeleaf(Model model) {
		model.addAttribute("name", "sunjingjun");
		return "hello";
	}

	@Resource
	UserService userService;

	@RequestMapping("/db/get")
	@ResponseBody
	public Result<User> dbGet() {
			User userById = userService.getUserById(1);
			return Result.success(userById);
	}

	@RequestMapping("db/tx")
	@ResponseBody
	public Result<Boolean> dbTx() {
		Boolean tx = userService.tx();
		return Result.success(tx);
	}

/*	@RequestMapping("redis/get")
	@ResponseBody
	public Result<Long> redisGet() {

	}*/

/*	@RequestMapping("redis/set")
	@ResponseBody
	public Result<String> redisSet() {
		*//*redisService.set("key2","hello,jingjun");
		String key2 = redisService.get("key2", String.class);
		return Result.success(key2);*//*
	}*/

/*	@RequestMapping("/mq")
	@ResponseBody
	public Result<String> mq() {
		mqSender.send("hello,jingjun");
		return Result.success("success");
	}

	@RequestMapping("/topic")
	@ResponseBody
	public Result<String> topic() {
		mqSender.sendTopic("hello,jingjun topic");
		return Result.success("success");
	}

	@RequestMapping("/fan")
	@ResponseBody
	public Result<String> fan() {
		mqSender.sendFanout("hello,jingjun");
		return Result.success("success");
	}

	@RequestMapping("/header")
	@ResponseBody
	public Result<String> header() {
		mqSender.sendHeader("hello,jingjun");
		return Result.success("success");
	}*/
}

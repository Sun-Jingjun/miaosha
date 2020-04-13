package com.jingjun.result;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	public static CodeMsg SERVER_E = new CodeMsg(500103, "未登录异常");
	public static CodeMsg PROCESS_TOO_MATCH = new CodeMsg(500103, "操作频繁");
	//登录模块 5002XX
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201,"密码为空");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500202,"手机号为空");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500203,"手机号格式错误");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500204,"手机号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205,"密码错误");
	public static CodeMsg MOBILE_NULL = new CodeMsg(500206,"无此手机号");
	public static CodeMsg SESSION_NULL = new CodeMsg(500207,"未登录");
	//商品模块 5003XX
	public static CodeMsg GOODS_NULL = new CodeMsg(500301,"商品售空");
	//订单模块 5004XX
	public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500401,"订单不存在");
	//秒杀模块 5005XX
	public static CodeMsg MIAO_SHA_REPEATE = new CodeMsg(500501,"已经秒杀过该商品");
	public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500502,"请求非法");
	public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500503,"秒杀出错");
	public static CodeMsg VERIFYCODE_ERROR = new CodeMsg(500504,"验证码错误");

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public CodeMsg fillArgs(Object...args) {
		this.msg = String.format(msg,args);
		return new CodeMsg(code,msg);
	}

	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}

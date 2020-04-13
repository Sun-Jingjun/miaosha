package com.jingjun.redis;

/**
 * 顶层接口，用于区分各模块的key，value
 */
public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}

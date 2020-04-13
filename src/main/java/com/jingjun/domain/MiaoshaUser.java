package com.jingjun.domain;

import java.util.Date;

public class MiaoshaUser {
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * md5辅助码
	 */
	private String salt;
	/**
	 * 头像，云存储的id
	 */
	private String head;
	/**
	 * 注册日期
	 */
	private Date registerDate;
	/**
	 * 上次登录时间
	 */
	private Date lastLoginDate;
	/**
	 * 登录次数
	 */
	private Integer loginCount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	@Override
	public String toString() {
		return "MiaoshaUser{" +
				"id=" + id +
				", nickname='" + nickname + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", head='" + head + '\'' +
				", registerDate=" + registerDate +
				", lastLoginDate=" + lastLoginDate +
				", loginCount=" + loginCount +
				'}';
	}
}

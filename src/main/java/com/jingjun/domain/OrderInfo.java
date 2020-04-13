package com.jingjun.domain;

import java.util.Date;

public class OrderInfo {
	/**
	 * 订单id
	 */
	private Long id;
	/**
	 * 购买用户id
	 */
	private Long userId;
	/**
	 * 购买商品id
	 */
	private Long goodsId;
	/**
	 * 配送地址
	 */
	private Long  deliveryAddrId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 购买商品数量
	 */
	private Integer goodsCount;
	/**
	 * 商品价格
	 */
	private Double goodsPrice;
	/**
	 * 购买渠道
	 * 1pc,2android,3ios
	 */
	private Integer orderChannel;
	/**
	 * 订单状态
	 * 0新建未支付，1待发货，2已发货，3已收货，4已退款，5已完成
	 */
	private Integer status;
	/**
	 * 下单时间
	 */
	private Date createDate;
	/**
	 * 支付时间
	 */
	private Date payDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Long getDeliveryAddrId() {
		return deliveryAddrId;
	}
	public void setDeliveryAddrId(Long deliveryAddrId) {
		this.deliveryAddrId = deliveryAddrId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Integer getOrderChannel() {
		return orderChannel;
	}
	public void setOrderChannel(Integer orderChannel) {
		this.orderChannel = orderChannel;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}

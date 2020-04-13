package com.jingjun.domain;

import java.util.Date;

public class MiaoshaGoods {
	/**
	 * 秒杀商品id
	 */
	private Long id;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 秒杀商品库存
	 */
	private Integer stockCount;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 秒杀价格
	 */
	private Double miaoshaPrice;
	/**
	 * 结束时间
	 */
	private Date endDate;

	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}

	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}

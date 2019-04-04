package com.tzj.collect.api.app.param;

import java.util.Date;

public class ArrivalTimeLogBean {
	private Long id;
	/**
	 * 订单id
	 */
	private Integer orderId;
	/**
	 * 更改之前的上门时间
	 */
	private String beforeDate;
	/**
	 * 更改之前 上午am 下午pm
	 */
	private String beforePeriod;
	/**
	 * 更改之后的上门时间
	 */
	private String afterDate;
	/**
	 * 更改之后 上午am 下午pm
	 */
	private String afterPeriod;
	/**
	 * 第几次更改
	 */
	private Integer num;
	
	/**
	 * 取消描述
	 */
	private String cancleDesc;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getBeforeDate() {
		return beforeDate;
	}
	public void setBeforeDate(String beforeDate) {
		this.beforeDate = beforeDate;
	}
	public String getBeforePeriod() {
		return beforePeriod;
	}
	public void setBeforePeriod(String beforePeriod) {
		this.beforePeriod = beforePeriod;
	}
	public String getAfterDate() {
		return afterDate;
	}
	public void setAfterDate(String afterDate) {
		this.afterDate = afterDate;
	}
	public String getAfterPeriod() {
		return afterPeriod;
	}
	public void setAfterPeriod(String afterPeriod) {
		this.afterPeriod = afterPeriod;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getCancleDesc() {
		return cancleDesc;
	}
	public void setCancleDesc(String cancleDesc) {
		this.cancleDesc = cancleDesc;
	}
	
	
}

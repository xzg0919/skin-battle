package com.tzj.collect.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 上门回收时间日志表
 * @author 王灿
 *
 */
@TableName("sb_arrival_time_log")
public class ArrivalTimeLog extends DataEntity<Long>{
	private Long id;
	/**
	 * 订单id
	 */
	private Integer orderId;
	/**
	 * 更改之前的上门时间
	 */
	private Date beforeDate;
	/**
	 * 更改之前 上午am 下午pm
	 */
	private String beforePeriod;
	/**
	 * 更改之后的上门时间
	 */
	private Date afterDate;
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
	
	/**
	 * 页面需要的创建时间
	 */
	@TableField(exist=false)
	private String datePage;
	
	
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
	public Object getBeforeDate() {
		if(null != beforeDate) {
			return this.getYMD(beforeDate);
		}
		return beforeDate;
	}
	public void setBeforeDate(Date beforeDate) {
		this.beforeDate = beforeDate;
	}
	public String getBeforePeriod() {
		return beforePeriod;
	}
	public void setBeforePeriod(String beforePeriod) {
		this.beforePeriod = beforePeriod;
	}
	public Object getAfterDate() {
		if(null != afterDate) {
			return this.getYMD(afterDate);
		}
		return afterDate;
	}
	public void setAfterDate(Date afterDate) {
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
	
	
	public String getDatePage() {
		return this.getDate(createDate);
	}
	public void setDatePage(String datePage) {
		this.datePage = datePage;
	}
	@Transactional
	public String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	private String getYMD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);

	}
}

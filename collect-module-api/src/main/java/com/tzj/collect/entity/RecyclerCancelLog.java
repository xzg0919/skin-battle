package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_recycle_cancel_log")
public class RecyclerCancelLog extends  DataEntity<Long>{
	
	private Long id;
	
	private String orderId;
	
	private String cancelDate;//取消时间
	
	private String recycleName;//回收人员姓名
	
	private String cancelReason;//取消原因
	
	private String recyclerId;//回收员id

	public Long getId() {
		return id;
	}

	
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getRecycleName() {
		return recycleName;
	}

	public void setRecycleName(String recycleName) {
		this.recycleName = recycleName;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getRecycleId() {
		return recyclerId;
	}

	public void setRecycleId(String recycleId) {
		this.recyclerId = recycleId;
	}
}

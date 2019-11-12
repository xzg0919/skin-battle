package com.tzj.collect.core.result.business;

public class CancelResult {
	
	private String tel; //电话
	private String cancelDate;//取消时间
	
	private String recycleName;//回收人员姓名
	
	private String cancelReason;//取消原因
	
	private String recycleId;//回收员id

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
		return recycleId;
	}

	public void setRecycleId(String recycleId) {
		this.recycleId = recycleId;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}
	
}

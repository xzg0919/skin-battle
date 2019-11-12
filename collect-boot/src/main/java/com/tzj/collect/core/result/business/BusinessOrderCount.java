package com.tzj.collect.core.result.business;

public class BusinessOrderCount {
	
	//待处理订单
	private Integer init = 0;
	//已派发订单
	private Integer send = 0;
	//进行中订单
	private Integer already = 0;
	//已完成订单
	private Integer complete = 0;
	//已取消订单
	private Integer cancel = 0;
	public Integer getInit() {
		return init;
	}
	public void setInit(Integer init) {
		this.init = init;
	}
	public Integer getSend() {
		return send;
	}
	public void setSend(Integer send) {
		this.send = send;
	}
	public Integer getAlready() {
		return already;
	}
	public void setAlready(Integer already) {
		this.already = already;
	}
	public Integer getComplete() {
		return complete;
	}
	public void setComplete(Integer complete) {
		this.complete = complete;
	}
	public Integer getCancel() {
		return cancel;
	}
	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}
	
	
	
}

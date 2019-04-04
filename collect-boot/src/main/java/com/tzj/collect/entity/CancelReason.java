package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
*
*取消原因Entity
*
* @Author 王灿
**/
@TableName("sb_cancel_reason")
public class CancelReason extends DataEntity<Long>{
	private Long id;

    /**
     * 原因
     */
    private Integer reason;
    /**
     * 取消类型，默认订单取消order
     */
    private Integer type;
    
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getReason() {
		return reason;
	}
	public void setReason(Integer reason) {
		this.reason = reason;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
    
}

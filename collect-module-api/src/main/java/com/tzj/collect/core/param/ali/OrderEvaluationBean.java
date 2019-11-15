package com.tzj.collect.core.param.ali;

/**
 * 评价Bean
 * @author Michael_Wang
 *
 */
public class OrderEvaluationBean {
	 private Long id;
	 /**
	  * 订单id
	 */
	 private  int orderId; 
	 
	 /**
	 * 回收人员id
	 */
	private  int recyclerId;
	/**
	   * 得分
	*/
	private  int score;
	/**
	 * 会员id
	*/
	private  int memberId;
	    
	/**
	 * 评价内容
	*/
	private  String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getRecyclerId() {
		return recyclerId;
	}

	public void setRecyclerId(int recyclerId) {
		this.recyclerId = recyclerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}

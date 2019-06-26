
/**
* @Title: SbOrderLog.java
* @Package com.tzj.collect.entity
* @Description: 【】
* @date 2018年3月5日 下午12:23:19
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
* @ClassName: SbOrderLog
* @Description: 【】
* @date 2018年3月5日 下午12:23:19
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@TableName("sb_order_log")
public class OrderLog  extends DataEntity<Long>{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int orderId;
	/**
	 * 操作动作的描述
	 */
	private String op;
	/**
	 * 记录订单被操作之前的订单状态
	 */
	private String opStatusBefore;
	/**
	 * 记录订单将要被操作成什么订单状态
	 */
	private String opStatusAfter;

	
	
	@Override
	public Long getId() {
		
		return id;
	}
	

	@Override
	public void setId(Long id) {
		
		this.id= id;
	}

	
	
	/**
	* @return orderId
	*/
	
	public int getOrderId() {
		return orderId;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	
	/**
	* @return opStatusAfter
	*/
	
	public String getOpStatusAfter() {
		return opStatusAfter;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setOpStatusAfter(String opStatusAfter) {
		this.opStatusAfter = opStatusAfter;
	}


	
	/**
	* @return opStatusBefore
	*/
	
	public String getOpStatusBefore() {
		return opStatusBefore;
	}


	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setOpStatusBefore(String opStatusBefore) {
		this.opStatusBefore = opStatusBefore;
	}


	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


		

}

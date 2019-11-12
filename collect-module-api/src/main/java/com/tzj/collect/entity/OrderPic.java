
/**
* @Title: SbOrderPic.java
* @Package com.tzj.collect.entity
* @Description: 【】
* @date 2018年3月5日 下午12:48:06
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
* @ClassName: SbOrderPic
* @Description: 【】
* @date 2018年3月5日 下午12:48:06
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@TableName("sb_order_pic")
public class OrderPic extends DataEntity<Long>{

	
	private static final long serialVersionUID = 1L;
	//id
	private Long id;
	//订单id
	private int orderId;
	//图片url
	private String picUrl;
	//小图url
	private String smallPic;
	//原图url
	private String origPic;
	
	
	@Override
	public Long getId() {
		
		return id;
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

	
	

	
	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getSmallPic() {
		return smallPic;
	}


	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}


	public String getOrigPic() {
		return origPic;
	}


	public void setOrigPic(String origPic) {
		this.origPic = origPic;
	}


	@Override
	public void setId(Long id) {
	this.id= id;
		
	}
	
	

}

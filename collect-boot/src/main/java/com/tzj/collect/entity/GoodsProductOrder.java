package com.tzj.collect.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Created on 2018-08-08
 * <p>Description: [商品表Entity]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [wangcan]
 * @version        1.0
 */
@TableName("sb_goods_product_order")
public class GoodsProductOrder extends DataEntity<Integer>{
	/**
     *  Id
     */
    private Integer id;
    
    /**
     *  快递单号
     */
    private String orderNum;
    /**
     *  快递公司
     */
    private String orderCompany;
    /**
     *  用户地址
     */
    private String address;
    /**
     *  商品Id
     */
    private String productId;
    /**
     *  用户Id
     */
    private Integer memberId;
    /**
     *  阿里UserId 
     */
    private String aliUserId;
    /**
     *  实物名称
     */
    private String productName;
    /**
     *  用户名称
     */
    private String userName;
    /**
     *  用户联系方式
     */
    private String mobile;
    /**
     *  实物图片链接
     */
    private String productUrl;
    /**
     *  快递状态
     */
    private GoodsState goodsState;
    
    


	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public String getOrderNum() {
		return orderNum;
	}




	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}




	public String getOrderCompany() {
		return orderCompany;
	}




	public void setOrderCompany(String orderCompany) {
		this.orderCompany = orderCompany;
	}




	public String getAddress() {
		return address;
	}




	public void setAddress(String address) {
		this.address = address;
	}



	public String getProductId() {
		return productId;
	}




	public void setProductId(String productId) {
		this.productId = productId;
	}




	public Integer getMemberId() {
		return memberId;
	}




	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}



	public String getAliUserId() {
		return aliUserId;
	}




	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}




	public String getProductName() {
		return productName;
	}




	public void setProductName(String productName) {
		this.productName = productName;
	}




	public String getProductUrl() {
		return productUrl;
	}




	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}




	public GoodsState getGoodsState() {
		return goodsState;
	}




	public void setGoodsState(GoodsState goodsState) {
		this.goodsState = goodsState;
	}


	public String getUserName() {
		return userName;
	}




	public void setUserName(String userName) {
		this.userName = userName;
	}




	public String getMobile() {
		return mobile;
	}




	public void setMobile(String mobile) {
		this.mobile = mobile;
	}




	public enum GoodsState implements IEnum {
		INIT(0), // 待发货
		TOSEND(1), // 已发货
		COMPLETE(2), // 已完成
		CANCEL(3); // 已取消
		private int value;

		GoodsState(final int value) {
			this.value = value;
		}

		public Serializable getValue() {
			return this.value;
		}
	}
}

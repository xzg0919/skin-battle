package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @ClassName: IotOrder
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 *
 * 四分类IOT订单
 */
@Data
@TableName("sb_iot_order")
public class IotOrder extends DataEntity<Long> {
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * 订单号
	 */
	String orderNo;

	/**
	 * 订单状态
	 */

	@TableField(value = "status_")
	private IotOrder.OrderStatus status = OrderStatus.PROCESSING;

	public enum OrderStatus implements IEnum {
		/**
		 * 正在投递中
		 */
		PROCESSING(0),
		/**
		 * 已完成
		 */
		COMPLETE(1),
		/**
		 * 已取消
		 */
		CANCEL(2);

		private int value;

		OrderStatus(final int value) {
			this.value = value;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}
	}


	String aliUserId;


	/**
	 * 设备编号
	 */
	String deviceCode;

	/**
	 * 设备地址
	 */
	String deviceAddress;


	/**
	 * 公司编号
	 */
	Long  companyId;

	/**
	 * 公司名称
	 */
	String  companyName;


	/**
	 * 订单类型
	 */
	 OrderType orderType;


	public enum OrderType implements IEnum {
		/**
		 * 积分
		 */
		POINT(0),
		/**
		 * 森林能量
		 */
		MYSL(1),
		/**
		 * 钱
		 */
		MONEY(2);

		private int value;

		OrderType(final int value) {
			this.value = value;
		}

		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	/**
	 * 获得权益的数量  可能是积分 可能是钱  根据订单类型来判断
	 */
    BigDecimal amount;


	public IotOrder() {
	}

	public IotOrder(String orderNo,   String aliUserId, String deviceCode, String deviceAddress, Long companyId, String companyName) {
		this.orderNo = orderNo;
		this.aliUserId = aliUserId;
		this.deviceCode = deviceCode;
		this.deviceAddress = deviceAddress;
		this.companyId = companyId;
		this.companyName = companyName;
	}
	/**
	 * 蚂蚁深林能量订单Id
	 */
	private String myslOrderId;

	/**
	 * 蚂蚁森林请求参数
	 */
	private String myslParam;

}

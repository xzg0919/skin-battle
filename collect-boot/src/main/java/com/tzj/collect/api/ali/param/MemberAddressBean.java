package com.tzj.collect.api.ali.param;

import lombok.Data;

/**
 * 会员地址
 * @author Administrator
 *
 */
@Data
public class MemberAddressBean {
	
	 private String id;
	 /**
	  * 会员id
	  */
	 private String memberId;
	 /**
	  * 区域Id
	  */
	 private String areaId;
	/**
	 * 区域名字
	 */
	private String areaName;
	 /**
	  * 街道Id
	  */
	 private String streetId;
	/**
	 * 街道名字
	 */
	private String streetName;
	 /**
	  * 小区Id
	  */
	 private String communityId;
	/**
	 * 小区名字
	 */
	private String communityName;
	 
	 /**
	  * 用户输入小区地址
	  */
	 private String commByUserInput;
	 /**
	  * 地址
	  */
	 private String address;
	 /**
	  * 门牌编号
	  */
	 private String houseNumber;
	 /**
	  * 电话
	  */
	 private String tel;
	 /**
	  * 是否是默认 1代表是默认
	  */
	 private String isSelected;
	 /**
	  * 姓名
	  */
	 private String name;
	 /**
	  * 市级Id
	  */
	 private Integer cityId;
	/**
	 * 用户保存地图获取名字
	 */
	private String mapName;
	/**
	 * 用户点击地图地址
	 */
	private String mapAddress;



	
}

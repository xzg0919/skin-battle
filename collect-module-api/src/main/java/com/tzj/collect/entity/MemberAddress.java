package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
*
*会员地址表
*
* @Author 王灿
**/
@TableName("sb_member_address")
@Data
public class MemberAddress extends  DataEntity<Long>{
     private Long id;
	 /**
	  * 会员id
	  */
	 private String memberId;
	/**
	 * 阿里user_id 用户唯一标识
	 */
	private String aliUserId;
	 /**
	  * 区域Id
	  */
	 private Integer areaId;
	 /**
	  * 街道Id
	  */
	 private Integer streetId;
	 /**
	  * 小区Id
	  */
	 private Integer communityId;
	 /**
	  * 地址
	  */
	 @TableField(value="address_")
	 private String address;
	 /**
	  * 用户输入小区地址
	  */
	 private String commByUserInput;
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
	 private Integer isSelected;
	 /**
	  * 姓名
	  */
	 @TableField(value="name_")
	 private String name;
	 /**
	  * 市级Id
	  */
	 private Integer cityId;
	 
	 /**
	  * 是否有定点回收
	  */
	 @TableField(exist = false)
	 private String isFixedPoint;
	/**
	 * 是否回收电器
	 */
	@TableField(exist = false)
	private String isDigital;
	/**
	 * 是否回收大件
	 */
	@TableField(exist = false)
	private String isDigThing;
	/**
	 * 是否回收生活垃圾
	 */
	@TableField(exist = false)
	private String isHousehold;
	/**
	 * 是否回收5公斤废纺衣物
	 */
	@TableField(exist = false)
	private String isFiveKg;
	/**
	 * 城市名称
	 */
	private String cityName;
	/**
	 * 行政区名称
	 */
	private String areaName;
	/**
	 * 街道名称
	 */
	private String streetName;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 国际街道编码
	 */
	private String townCode;
	/**
	 * 用户保存地图获取名字
	 */
	private String mapName;
	/**
	 * 用户点击地图地址
	 */
	private String mapAddress;

	@TableField(exist = false)
	private String tableName;

	private Integer provinceId;

	private String provinceName;

	@TableField(exist = false)
	private String fullAddress;
}

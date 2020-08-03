package com.tzj.collect.core.param.ali;


import com.tzj.collect.core.param.business.CommunityBean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 区域对象
 * @author Michael_Wang
 *
 */
@Data
public class AreaBean {
	//id
	private Integer id;
	
	//level
	private Integer level;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 行政市Id 例上海市Id
	 */
	private String cityId;
	/**
	 * 行政区名字
	 */
	private String areaName;
	/**
	 * 街道名字
	 */
	private String streetName;
	/**
	 * 街道Id
	 */
	private String streeId;
	/**
	 * 区域Id
	 */
	private String areaId;
	/**
	 * 0保存，1删除
	 */
	private String saveOrDelete;

	private String provinceId;

	private PageBean pageBean;

	private String companyId;

	private List<String> streetList;

	private List<String> titleList;

	private List<CommunityBean> communityIdList;

	private String ratio; //城市系数

	private String parentId;


	
	
}

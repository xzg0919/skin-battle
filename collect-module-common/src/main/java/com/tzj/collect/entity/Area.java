package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_area")
public class Area extends DataEntity<Long> {
	private Long id;
	private Integer parentId;   //父级编号
	private String parentIds;   //所有父级编号
	private String areaName;    //名称
	@TableField(value="sort_")
	private Integer sort;      //排序
	@TableField(value="code_")
	private String code;        //区域编码
	private String type;		//区域类型
	@TableField(exist=false)
    private Area area;
	@TableField(exist=false)
	private String name;

	public String getName() {
		return areaName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort_(Integer sort) {
		this.sort = sort;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

}

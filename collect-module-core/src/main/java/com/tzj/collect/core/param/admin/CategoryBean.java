package com.tzj.collect.core.param.admin;

import com.baomidou.mybatisplus.annotations.TableField;
import com.tzj.collect.entity.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CategoryBean {
	private Long id;
	private Integer parentId;  //父级编号
	private String parentIds;  //所有父级编号
	@TableField(value="name_")
	private String name;      //名称
	@TableField(value="sort_")
	private int sort;      //排序
	@TableField(value="code_")
	private String code;    //分类编码
	@TableField(value="level_")
	private int level;     //层级
	private String icon;    //图标
	private BigDecimal price;    //基准价
	private BigDecimal marketPrice;   //市场价
	private String unit;    //计量单位
	private String ismetering;   //是否计量
    private Category category;

    private String companyId;
    private String cityId;
    private String title;
    private List<String> paramList;
    private String isOpen; //0开通  1关闭


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIsmetering() {
		return ismetering;
	}
	public void setIsmetering(String ismetering) {
		this.ismetering = ismetering;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
    
    
    
}

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@TableName("sb_category")
public class Category extends DataEntity<Long> {
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
	@TableField(exist=false)
	private CompanyCategory companyCategory;
	@TableField(exist=false)
    private Category category;
	@TableField(exist=false)
    private Integer count;//对应的子分类条数
	@TableField(value = "title")
	private CategoryType title;//最上层
	@TableField(exist=false)
	private Integer categoryId;

	private String recNotes;
	
	private String recTypeExp;
	
	/**
	 * 设置的绿色能量
	 */
	private Integer greenCount;
	/**
	 * 以旧换新 0不支持 1只支持以旧换新 2两种都支持
	 */
	private String isOldExchangeNew;
	/**
	 * 以旧换新的图片说明连接
	 */
	private String oldExchangeNewPic;

	private String aliItemType;

	private String antForestPic;

	public String getAliItemType() {
		return aliItemType;
	}

	public void setAliItemType(String aliItemType) {
		this.aliItemType = aliItemType;
	}

	public String getAntForestPic() {
		return antForestPic;
	}

	public void setAntForestPic(String antForestPic) {
		this.antForestPic = antForestPic;
	}

	public String getIsOldExchangeNew() {
		return isOldExchangeNew;
	}

	public void setIsOldExchangeNew(String isOldExchangeNew) {
		this.isOldExchangeNew = isOldExchangeNew;
	}

	public String getOldExchangeNewPic() {
		return oldExchangeNewPic;
	}

	public void setOldExchangeNewPic(String oldExchangeNewPic) {
		this.oldExchangeNewPic = oldExchangeNewPic;
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

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public CategoryType getTitle() {
		return title;
	}

	public void setTitle(CategoryType title) {
		this.title = title;
	}
	
	public CompanyCategory getCompanyCategory() {
		return companyCategory;
	}

	public void setCompanyCategory(CompanyCategory companyCategory) {
		this.companyCategory = companyCategory;
	}

	public Integer getGreenCount() {
		return greenCount;
	}

	public void setGreenCount(Integer greenCount) {
		this.greenCount = greenCount;
	}

	public Object getRecNotes() {
		if(StringUtils.isNotBlank(recNotes)) {
			String [] rec = recNotes.split(",");
			if(rec.length!=0) {
				List<String> list = new ArrayList<String>();
				for (String s : rec) {
					list.add(s);
				}
				return list;
			}
		}
		return recNotes;
	}

	public void setRecNotes(String recNotes) {
		this.recNotes = recNotes;
	}

	public String getRecTypeExp() {
		return recTypeExp;
	}

	public void setRecTypeExp(String recTypeExp) {
		this.recTypeExp = recTypeExp;
	}
	public String getPriceAndUnit() {
		return "￥" +  price + "/" + unit;
	}

	public enum CategoryType implements IEnum{
    	DEFUALT(0),   	 //初始值
    	DIGITAL(1),		//家电数码
    	HOUSEHOLD(2),	//生活垃圾
		BIGTHING(3);	//大件垃圾

    	private int value;

    	CategoryType(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
//	public String getUnit4Page(){
//		StringBuilder builder = null;
//		if ("1".equals(ismetering)) {
//			builder =  new StringBuilder("￥ ");
//			builder.append(price.toString());
//			builder.append("/");
//			builder.append(unit);
//		}else{
//			return null;
//		}
//		return builder.toString();
//	}
}

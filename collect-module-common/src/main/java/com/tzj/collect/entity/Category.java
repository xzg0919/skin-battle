package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableName("sb_category")
@Data
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

	@TableField(exist=false)
	private List<Map<String,Object>> categoryMap;

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
		FIVEKG(3),		//5公斤废纺衣物回收
		BIGTHING(4),	//大件垃圾
		IOTORDER(5);//iot设备

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

	/**
	 * 对外IOT设备，一级类型(以code对应)
	 * @author sgmark@aliyun.com
	 * @date 2019/3/30 0030
	 * @param
	 * @return
	 */
	public enum ParentType implements IEnum{
		PAPER("001"),   	 //纸类
		PLASTIC("002"),		//塑料类
		GLASS("003"),	//玻璃类
		FABRIC("004"),		//纺织类
		METAL("006"),	//金属类
		OTHER("007"); //其他类型

		private String value;

		ParentType(final String value) {
			this.value = value;
		}

		public Serializable getValue() {
			return this.value;
		}
	}
	/**
	 * 对外IOT设备，二级类型(以code对应)
	 * @author sgmark@aliyun.com
	 * @date 2019/3/30 0030
	 * @param
	 * @return
	 */
	public enum SecondType implements IEnum{
		//纸类(PAPER)
		BOOK_MAGAZINE("001", "0011"),   	 //书本杂志
		CARD_BOARD_BOXES("001", "0015"),   	 //纸板箱
		NEWS_PAPER("001", "0016"),   	 //报纸
		PACKING_BOX("001", "0017"),		//包装盒
		WASTE_PAPER("001", "0018"),	//杂纸

		//塑料类(PLASTIC)
		BEVERAGE_BOTTLES("002", "0021"),		//饮料瓶
		PLASTIC_TOYS("002", "0022"),	//玩具
		KETTLE_BARREL("002", "0023"), //油壶油桶
		FOAM("002", "0024"),	//泡沫塑料
		MISCELLANEOUS_PLASTICS("002", "0025"),	//杂塑料

		//玻璃类(GLASS)
		GLASS_BOTTLE("003", "0031"),//玻璃瓶
		BLOCKS_GLASS("003", "0032"),//成块玻璃
		SHREDDED_GLASS("003", "0033"),//碎玻璃
		GLASS_PRODUCTS("003", "0034"),//其他玻璃制品

		//纺织类(FABRIC)
		CLOTHES("004", "0041"),//衣服
		PANTS("004", "0042"),//裤子
		SHOES("004", "0043"),//鞋子
		SKIRT("004", "0044"),//裙子
		BAG("004", "0045"),//包袋

		//金属类(METAL)
		CANS("006", "0061"),//易拉罐
		POWER_CORD("006", "0065"),	//电源线
		OTHERS_METAL("006","0066"),//其他废旧金属

		//其他类型(OTHER)
		OTHERS("007", "0071");	//其他类型(暂存六废)
		private String key;

		private String value;

		SecondType(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getValueByKey(String key) {
			SecondType[] enums = SecondType.values();
			for (int i = 0; i < enums.length; i++) {
				if (enums[i].getKey().equals(key)) {
					return enums[i].getValue();
				}
			}
			return "";
		}
	}
}

package com.tzj.collect.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CityType implements BaseEnum<CityType, String>{
	/**
     * 
     */
	SH("021-index", "上海市"),
	/**
     * 
     */
	NJ("025-index", "南京市"),
	/**
     * 
     */
	WX("0510-index", "无锡市"),
	 /**
     * 
     */
	CZ("0519-index", "常州市"),
	 /**
     * 
     */
	SZ("0512-index", "苏州市"),
	 /**
     * 
     */
	NT("0513-index", "南通市"),
	 /**
     * 
     */
	YZ("0514-index", "扬州市"),
	 /**
     * 
     */
	ZJ("0511-index", "镇江市"),
	 /**
     * 
     */
	TZ("0523-index", "泰州市"),
	 /**
     * 
     */
	HZ("0571-index", "杭州市"),
    /**
     * 
     */
	NB("0574-index", "宁波市"),
    /**
     * 
     */
	JX("0573-index", "嘉兴市"),
    /**
     * 
     */
	JH("0579-index", "金华市"),
	/**
     * 
     */
	HZS("0572-index", "湖州市");
	
    
    /**
     * 所有商品类型集合
     */
    private static Map<String, CityType> enumMap = new HashMap<String, CityType>();

    /**
     * value值
     */
    private String value;

    /**
     * 中文值
     */
    private String nameCN;

    /**
     * 
     * <p>Discription:[构造器方法]</p>
     * @coustructor 方法.
     */
    CityType(final String value, final String nameCN)
    {
        this.value = value;
        this.nameCN = nameCN;
    }

    
    static
    {
        for (CityType pt : CityType.values())
        {
            enumMap.put(pt.getValue(), pt);
        }
    }

    /**
     * <p>Created on 2017年8月12日</p>
     * <p>Description:[所有的商品类型]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List
     */
    public static List<CityType> getALL()
    {
        return new ArrayList<CityType>(enumMap.values());
    }

    /**
     *  Created on 2017年2月8日 
     * <p>Discription:[根据value获取]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param value
     * @return OrderStatus
     */
    public static CityType getEnum(final String value)
    {
        return enumMap.get(value);
    }

    /**
     *  Created on 2017年2月10日
     * <p>Discription:[获取商品类型value值]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String getValue()
    {
        return value;
    }

    /**
     *  Created on 2017年2月10日
     * <p>Discription:[获取商品类型中文值]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String getNameCN()
    {
        return nameCN;
    }

    /**
     * <p>Discription:[设置商品类型value值]</p>
     * @param value 
     */
    public void setValue(final String value)
    {
        this.value = value;
    }

    /**
     * <p>Discription:[设置商品类型中文值]</p>
     * @param nameCN 
     */
    public void setNameCN(final String nameCN)
    {
        this.nameCN = nameCN;
    }
}

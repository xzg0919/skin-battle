package com.tzj.collect.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TtemType implements BaseEnum<CityType, String>{
    /**
     *
     */
    appliance("appliance", "⼤家电"),
    /**
     *
     */
    cellphone("cellphone", "手机"),
    /**
     *
     */
    laptop("laptop", "笔记本"),
    /**
     *
     */
    camera("camera", "相机"),
    /**
     *
     */
    clothes("clothes", "衣服"),
    /**
     *
     */
    book("book", "图书"),
    /**
     *
     */
    cans("cans", "易拉罐"),
    /**
     *
     */
    paper("paper", "纸类"),
    /**
     *
     */
    plastic("plastic", "塑料类"),
    /**
     *
     */
    metal("metal", "金属"),
    /**
     *
     */
    fabric("fabric", "织物");

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
    TtemType(final String value, final String nameCN)
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

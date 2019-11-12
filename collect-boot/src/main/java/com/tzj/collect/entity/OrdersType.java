package com.tzj.collect.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public enum OrdersType implements BaseEnum<OrdersType, String>
{

    DIGITAL("1", "家电数码"),

    HOUSEHOLD("2", "生活垃圾"),

    FIVEKG("3", "5公斤废纺衣物回收"),

    BIGTHING("4", "大件垃圾"),

    IOTORDER("5", "iot设备");
    /* shop_money("shop_money", "全场代金券"),
    shop_discount("shop_discount", "全场折扣券"),
    product_money("product_money", "商品代金券"),
    product_discount("product_discount", "商品折扣券");*/
    /**
     * 所有类型集合
     */
    private static Map<String, Object> enumMap = new HashMap<String, Object>();

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
    OrdersType(final String value, final String nameCN)
    {
        this.value = value;
        this.nameCN = nameCN;
    }
    
    static
    {
        for (OrdersType vt : OrdersType.values())
        {
            enumMap.put(vt.getValue(), vt.nameCN);
        }
    }

    /**
     * 
     * <p>Created on 2017年8月12日</p>
     * <p>Description:[所有的卡券类型]</p>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List
     */
    public static Object getALL()
    {
        return enumMap.values();
    }

    /**
     *  Created on 2017年2月8日 
     * <p>Discription:[根据value获取]</p>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param value
     * @return OrderStatus
     */
    public static Object getEnum(final String value)
    {
        return enumMap.get(value);
    }

    /**
     *  Created on 2017年2月10日
     * <p>Discription:[获取卡券类型value值]</p>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String getValue()
    {
        return value;
    }

    /**
     *  Created on 2017年2月10日
     * <p>Discription:[获取卡券类型中文值]</p>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String getNameCN()
    {
        return nameCN;
    }

    /**
     * <p>Discription:[设置卡券类型value值]</p>
     * @param value 
     */
    public void setValue(final String value)
    {
        this.value = value;
    }

    /**
     * <p>Discription:[设置卡券类型中文值]</p>
     * @param nameCN 
     */
    public void setNameCN(final String nameCN)
    {
        this.nameCN = nameCN;
    }

    public static Object typeMapList(){
        return enumMap;
    }

}

package com.tzj.collect.entity;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusType implements BaseEnum<OrderStatusType, String> {

    INIT("1", "待接单"),

    TOSEND("2", "已派送"),

    ALREADY("3", "已接单"),

    COMPLETE("4", "已完成"),

    CANCEL("5", "已取消"),

    REJECTED("6", "平台驳回");

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
    OrderStatusType(final String value, final String nameCN)
    {
        this.value = value;
        this.nameCN = nameCN;
    }

    static
    {
        for (OrderStatusType vt : OrderStatusType.values())
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

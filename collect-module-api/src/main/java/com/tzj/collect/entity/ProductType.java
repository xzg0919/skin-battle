package com.tzj.collect.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created on 2017年2月10日
 * <p>Title:       [支付宝绿账系统]_[]_[]</p>
 * <p>Description: [商品类型]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
*/
public enum ProductType implements BaseEnum<ProductType, String>
{
    /**
     * 
     */
    voucher("voucher", "卡券商品"),
    /**
     * 
     */
    goods("goods", "实物商品"),

    D("D", "折扣"),

    P("P", "比例"),

    R("R", "满减"),

    E("E", "提价");
    
    /**
     * 所有商品类型集合
     */
    private static Map<String, ProductType> enumMap = new HashMap<String, ProductType>();

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
    ProductType(final String value, final String nameCN)
    {
        this.value = value;
        this.nameCN = nameCN;
    }

    
    static
    {
        for (ProductType pt : ProductType.values())
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
    public static List<ProductType> getALL()
    {
        return new ArrayList<ProductType>(enumMap.values());
    }

    /**
     *  Created on 2017年2月8日 
     * <p>Discription:[根据value获取]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param value
     * @return OrderStatus
     */
    public static ProductType getEnum(final String value)
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

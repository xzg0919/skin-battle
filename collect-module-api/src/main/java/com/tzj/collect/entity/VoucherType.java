package com.tzj.collect.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created on 2017年2月10日
 * <p>Title:       [支付宝绿账系统]_[]_[]</p>
 * <p>Description: [卡券类型]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
*/
public enum VoucherType implements BaseEnum<VoucherType, String>
{
    /**
     * 
     */
    money("money", "代金券"),
    /**
     * 
     */
    discount("discount", "折扣券"),
	/**
	 * 
	 */
	outvoucharA("outvoucharA", "外部券"),
	/**
	 * 
	 */
	outvoucharB("outvoucharB", "外部组合券");
    /* shop_money("shop_money", "全场代金券"),
    shop_discount("shop_discount", "全场折扣券"),
    product_money("product_money", "商品代金券"),
    product_discount("product_discount", "商品折扣券");*/
    /**
     * 所有卡券类型集合
     */
    private static Map<String, VoucherType> enumMap = new HashMap<String, VoucherType>();

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
    VoucherType(final String value, final String nameCN)
    {
        this.value = value;
        this.nameCN = nameCN;
    }
    
    static
    {
        for (VoucherType vt : VoucherType.values())
        {
            enumMap.put(vt.getValue(), vt);
        }
    }

    /**
     * 
     * <p>Created on 2017年8月12日</p>
     * <p>Description:[所有的卡券类型]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List
     */
    public static List<VoucherType> getALL()
    {
        return new ArrayList<VoucherType>(enumMap.values());
    }

    /**
     *  Created on 2017年2月8日 
     * <p>Discription:[根据value获取]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param value
     * @return OrderStatus
     */
    public static VoucherType getEnum(final String value)
    {
        return enumMap.get(value);
    }

    /**
     *  Created on 2017年2月10日
     * <p>Discription:[获取卡券类型value值]</p>
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
     * <p>Discription:[获取卡券类型中文值]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
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

}

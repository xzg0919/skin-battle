package com.tzj.collect.entity;
/**
 * Created on 2017年2月8日
 * <p>Title:       [支付宝绿账系统]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
*/
public interface BaseEnum<E extends Enum<?>, T>
{
    public T getValue();
    
    public String getNameCN();
}
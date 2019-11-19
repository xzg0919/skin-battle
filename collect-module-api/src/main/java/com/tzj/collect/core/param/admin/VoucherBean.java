package com.tzj.collect.core.param.admin;

import lombok.Data;

/**
 * 
 * <p>Created on 2019年10月27日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [券的bean]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
@Data
public class VoucherBean
{
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 会员券id
     */
    private Long voucherMemberId;

}

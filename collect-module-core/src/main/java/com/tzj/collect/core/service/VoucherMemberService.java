package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.collect.entity.VoucherNofity;

/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [会员优惠券service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherMemberService extends IService<VoucherMember>
{

    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[发券]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    VoucherNofity send(VoucherNofity voucherNofity);

}
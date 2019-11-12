package com.tzj.collect.core.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.VoucherMember;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [会员优惠券映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherMemberMapper extends BaseMapper<VoucherMember>
{

    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[下单选择券]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Object
     */
    List<VoucherMember> getVoucherForOrder(Long memberId);

}
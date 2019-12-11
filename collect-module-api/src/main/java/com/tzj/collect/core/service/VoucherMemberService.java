package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.VoucherBean;
import com.tzj.collect.entity.Payment;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.collect.entity.VoucherNofity;

import java.math.BigDecimal;
import java.util.List;

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
    /*
    更新发放券的状态
     */
    String updateOrderNo(BigDecimal price,Integer orderId,String voucherId, Payment payment);
    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[券的使用]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    String voucherUse(VoucherBean voucherBean);
    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[下单选择券]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<VoucherMember>
     */
    List<VoucherMember> getVoucherForOrder(Long memberId);
    /**
     * 更新券为使用中的状态（即绑定状态）
     * @param orderId
     * @param orderNo
     * @param voucherMemberId
     * @return
     */
    boolean updateVoucherUseing(long orderId,String orderNo,String aliUserId,long voucherMemberId);
    /**
     * 更新券为可使用的状态（即领取待使用状态）
     * @param voucherMemberId
     * @return
     */
    boolean updateVoucherCreate(long voucherMemberId);
    
    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[重发券--领券再授权的用户]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    String reSend(String aliUserId);
    /**
     * 
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[使用复活卡]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void useRevive(String id);
    /**
     * 
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[没用过的复活卡的id]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<String>
     */
    List<VoucherMember> getReviveIdList(Long memberId);

}
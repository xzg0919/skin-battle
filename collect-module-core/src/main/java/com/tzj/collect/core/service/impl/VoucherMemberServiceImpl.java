package com.tzj.collect.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.VoucherMemberMapper;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.core.service.VoucherNofityService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.VoucherAli;
import com.tzj.collect.entity.VoucherCode;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.collect.entity.VoucherNofity;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [会员优惠券service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VoucherMemberServiceImpl extends ServiceImpl<VoucherMemberMapper, VoucherMember> implements VoucherMemberService
{
    @Autowired
    private VoucherMemberMapper voucherMemberMapper;
    @Autowired
    private VoucherAliService voucherAliService;
    @Autowired
    private VoucherCodeService voucherCodeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private VoucherNofityService voucherNofityService;
    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[发券]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    @Transactional
    public VoucherNofity send(VoucherNofity voucherNofity)
    {
        VoucherMember voucherMember = new VoucherMember();
        VoucherCode voucherCode = null;
        Member member = null;
        try
        {
            voucherCode = voucherCodeService.getByCode(voucherNofity.getEntityNum());
            if(null == voucherCode)
            {
                voucherNofity.setNotifyStatus("error");
                voucherNofity.setNotifyRemark("券码不存在");
                voucherNofityService.updateById(voucherNofity);
                return voucherNofity;
            }
            if(null != voucherCode.getMemberId())
            {
                voucherNofity.setNotifyStatus("error");
                voucherNofity.setNotifyRemark("券码已被领取,member:"+voucherCode.getMemberId());
                voucherNofityService.updateById(voucherNofity);
                return voucherNofity;
            }
            member = memberService.findMemberByAliId(voucherNofity.getUid());
            if(null == member)
            {
                voucherNofity.setNotifyStatus("error");
                voucherNofity.setNotifyRemark(voucherNofity.getUid() + "--此会员不存在");
                voucherNofityService.updateById(voucherNofity);
                return voucherNofity;
            }
            voucherCode.setMemberId(member.getId());
            voucherMember.setMemberId(member.getId());
            voucherMember.setAliUserId(member.getAliUserId());
            voucherMember.setVoucherStatus("CREATE");
            // 券内容
            voucherMember.setCreateBy("ali");
            voucherMember.setCreateDate(voucherNofity.getCreateDate());
            voucherMember.setDelFlag("0");
            voucherMember.setDis(voucherCode.getDis());
            voucherMember.setLowMoney(voucherCode.getLowMoney());
            voucherMember.setMoney(voucherCode.getMoney());
            voucherMember.setPickLimitTotal(voucherCode.getPickLimitTotal());
            voucherMember.setPickupEnd(voucherCode.getPickupEnd());
            voucherMember.setPickupStart(voucherCode.getPickupStart());
            voucherMember.setTopMoney(voucherCode.getTopMoney());
            voucherMember.setUpdateBy("ali");
            voucherMember.setUpdateDate(voucherNofity.getCreateDate());
            voucherMember.setValidDay(voucherCode.getValidDay());
            voucherMember.setValidEnd(voucherCode.getValidEnd());
            voucherMember.setValidStart(voucherCode.getValidStart());
            voucherMember.setValidType(voucherCode.getValidType());
            voucherMember.setVoucherCode(voucherCode.getVoucherCode());
            voucherMember.setVoucherId(voucherCode.getVoucherId());
            voucherMember.setVoucherName(voucherCode.getVoucherName());
            voucherMember.setVoucherType(voucherCode.getVoucherType());
            voucherMember.setOrderType(voucherCode.getOrderType());
            voucherCodeService.updateMemberId(voucherCode.getId(),voucherCode.getMemberId());
            this.insert(voucherMember);
            voucherAliService.updatePickCount(voucherCode.getVoucherId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            voucherNofity.setNotifyStatus("error");
            voucherNofity.setNotifyRemark(e.getMessage());
        }
        
        return voucherNofity;
        
        
        
        
    }

}
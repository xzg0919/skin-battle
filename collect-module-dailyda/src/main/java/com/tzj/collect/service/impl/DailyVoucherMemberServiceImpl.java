package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.VoucherConst;
import com.tzj.collect.core.service.DailyVoucherMemberService;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.collect.mapper.DailyVoucherMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 答答答复活券
 *
 * @author sgmark
 * @create 2019-12-18 14:53
 **/
@Service
@Transactional(readOnly=true)
public class DailyVoucherMemberServiceImpl extends ServiceImpl<DailyVoucherMemberMapper, VoucherMember> implements DailyVoucherMemberService {

    /**
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[使用复活卡]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void useRevive(String aliId)
    {
        VoucherMember voucherMember = null;
        EntityWrapper<VoucherMember> wrapper = new EntityWrapper<VoucherMember>();
        wrapper.eq("ali_user_id", aliId);
        wrapper.eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE);
        wrapper.orderBy("id", true);
        wrapper.last(" LIMIT 0,1 ");
        List<VoucherMember> voucherMemberList =  this.selectList(wrapper);
        if(null != voucherMemberList && !voucherMemberList.isEmpty())
        {
            voucherMember = voucherMemberList.get(0);
            voucherMember.setVoucherStatus(VoucherConst.VOUCHER_STATUS_USED);
            this.updateById(voucherMember);
        }
    }

    /**
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[没用过的复活卡的id]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return
     */
    @Override
    public List<VoucherMember> getReviveIdList(String aliId)
    {
        List<VoucherMember> voucherMemberList =  this.selectList(new EntityWrapper<VoucherMember>().eq("ali_user_id", aliId).eq("voucher_status",
                VoucherConst.VOUCHER_STATUS_CREATE).eq("voucher_type",VoucherConst.VOUCHER_TYPE_REVIVE));
        return voucherMemberList;
    }
    /**
     *
     * <p>Created on 2019年12月3日</p>
     * <p>Description:[没用过的复活卡的数量]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<String>
     */
    @Override
    public Integer getReviveCount(String aliId)
    {
        List<VoucherMember> voucherMemberList = getReviveIdList(aliId);
        if(null == voucherMemberList || voucherMemberList.isEmpty())
        {
            return 0;
        }
        return voucherMemberList.size();
    }
}

package com.tzj.collect.api.ali;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.VoucherBean;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

@ApiService
public class VoucherApi {


    @Autowired
    private VoucherMemberService voucherMemberService;


    /**
     * 小程序用户券列表
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "voucher.voucherAllList", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object voucherAllList(VoucherBean voucherBean){
        Member member = MemberUtils.getMember();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(date);
        Wrapper<VoucherMember> wrapper = new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId());
        String voucherType = voucherBean.getVoucherType();
        if("1".equals(voucherType)){
            wrapper.le("valid_start",now).ge("valid_end",now);
        }
        if ("2".equals(voucherType)){
            wrapper.eq("voucher_status","END");
        }
        if ("3".equals(voucherType)){
            wrapper.le("valid_end",now).eq("voucher_status","CREATE");
        }
        List<VoucherMember> voucherList = voucherMemberService.selectList(wrapper);
        return voucherList;
    }

    /**
     * 小程序用户可使用的券列表
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "voucher.voucherUseList", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object voucherUseList(VoucherBean voucherBean){
        Member member = MemberUtils.getMember();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(date);
        Wrapper<VoucherMember> wrapper = new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId());
        String voucherType = voucherBean.getVoucherType();
        String orderType = voucherBean.getOrderType();
        if ("1".equals(voucherType)){
            wrapper.le("valid_start",now).ge("valid_end",now).eq("voucher_status","CREATE");
        }else {
            wrapper.addFilter(" (valid_end < '"+now+"' OR voucher_status = 'END' OR valid_start > '"+now+"' OR voucher_status = 'USEING' )");
        }
        if ("bigFurniture".equals(orderType)){
            wrapper.eq("order_type","B");
        }
        List<VoucherMember> voucherList = voucherMemberService.selectList(wrapper);
        return voucherList;
    }



}

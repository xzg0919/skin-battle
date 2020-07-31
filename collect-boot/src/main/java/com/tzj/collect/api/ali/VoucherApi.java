package com.tzj.collect.api.ali;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.common.constant.VoucherConst;
import com.tzj.collect.core.param.ali.VoucherBean;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.VoucherMember;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

@ApiService
public class VoucherApi {


    @Autowired
    private VoucherMemberService voucherMemberService;
    @Autowired
    private VoucherAliService voucherAliService;


    /**
     * 小程序用户券列表
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "voucher.voucherAllList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object voucherAllList(VoucherBean voucherBean){
        Member member = MemberUtils.getMember();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(date);
        String voucherType = voucherBean.getVoucherType();
        Wrapper<VoucherMember> wrapper = new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId());
        Integer createCount = voucherMemberService.selectCount(new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId()).le("valid_start",now).ge("valid_end",now).eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE));
        Integer useCount = voucherMemberService.selectCount(new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId()).eq("voucher_status", VoucherConst.VOUCHER_STATUS_USED));
        Integer endCount = voucherMemberService.selectCount(new EntityWrapper<VoucherMember>().eq("ali_user_id", member.getAliUserId()).le("valid_end",now).eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE));
        if("1".equals(voucherType)){
            wrapper.le("valid_start",now).ge("valid_end",now).eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE);
        }
        if ("2".equals(voucherType)){
            wrapper.eq("voucher_status", VoucherConst.VOUCHER_STATUS_USED);
        }
        if ("3".equals(voucherType)){
            wrapper.le("valid_end",now).eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE);
        }
        List<VoucherMember> voucherList = voucherMemberService.selectList(wrapper);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("createCount",createCount);
        resultMap.put("useCount",useCount);
        resultMap.put("endCount",endCount);
        resultMap.put("voucherList",voucherList);
        return resultMap;
    }

    /**
     * 小程序用户可使用的券列表
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "voucher.voucherUseList", version = "1.0")
    @SignIgnore
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
            wrapper.addFilter(" (valid_end < '"+now+"' OR voucher_status = '"+ VoucherConst.VOUCHER_STATUS_USED +"' OR valid_start > '"+now+"' OR voucher_status = '"+ VoucherConst.VOUCHER_STATUS_USEING+"' )");
        }else {
            wrapper.le("valid_start",now).ge("valid_end",now).eq("voucher_status", VoucherConst.VOUCHER_STATUS_CREATE);
            if ("bigFurniture".equals(orderType)){
                wrapper.le("low_money",voucherBean.getPrice());
            }
        }
        if ("bigFurniture".equals(orderType)){
            wrapper.eq("order_type","B");
        }else if ("appliance".equals(orderType)){
            wrapper.eq("order_type","H");
        }else if ("rubbish".equals(orderType)){
            wrapper.eq("order_type","L");
        }
        List<VoucherMember> voucherList = voucherMemberService.selectList(wrapper);
        return voucherList;
    }
    /**
     * 计算优惠后的价格
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "voucher.getDiscountPrice", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getDiscountPrice(VoucherBean voucherBean){
        return voucherAliService.getDiscountPriceByVoucherId(new BigDecimal(voucherBean.getPrice()),voucherBean.getVoucherId());
    }


}

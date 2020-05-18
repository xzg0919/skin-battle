package com.tzj.collect.core.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.alipay.api.domain.AlipayMarketingVoucherStockUseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMarketingVoucherStockUseRequest;
import com.alipay.api.response.AlipayMarketingVoucherStockUseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.VoucherConst;
import com.tzj.collect.core.mapper.VoucherMemberMapper;
import com.tzj.collect.core.param.admin.VoucherBean;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.core.service.PaymentService;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.core.service.VoucherNofityService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Payment;
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
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    
    
    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[重发券--领券再授权的用户]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return
     */
    @Override
    @Transactional
    public String reSend(String aliUserId)
    {
        VoucherMember voucherMember = null;
        VoucherCode voucherCode = null;
        Member member = null;
        List<VoucherNofity> voucherNofityList = null;
        VoucherNofity voucherNofity = null;
        voucherNofityList = voucherNofityService.getListByAliId(aliUserId);
        if(null != voucherNofityList && !voucherNofityList.isEmpty())
        {
            for(int i=0,j=voucherNofityList.size();i<j;i++)
            {
                try
                {
                    voucherNofity = voucherNofityList.get(i);
                    if(!VoucherConst.VOUCHER_NOTIFY_MEMBER.equals(voucherNofity.getNotifyStatus()))
                    {
                        continue;
                    }
                    voucherCode = voucherCodeService.getByCode(voucherNofity.getEntityNum());
                    if(null == voucherCode)
                    {
                        voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_NO);
                        voucherNofity.setNotifyRemark("券码不存在");
                        voucherNofityService.updateStatus(voucherNofity);
                        continue;
                    }
                    if(null != voucherCode.getMemberId())
                    {
                        voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_HAD);
                        voucherNofity.setNotifyRemark("券码已被领取,member:"+voucherCode.getMemberId());
                        voucherNofityService.updateStatus(voucherNofity);
                        continue;
                    }
                    member = memberService.findMemberByAliId(aliUserId);
                    if(null == member)
                    {
                        voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_MEMBER);
                        voucherNofity.setNotifyRemark(voucherNofity.getUid() + "--此会员不存在");
                        voucherNofityService.updateStatus(voucherNofity);
                        continue;
                    }
                    voucherMember = new VoucherMember();
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
                    voucherMember.setVoucherCount(voucherCode.getVoucherCount());
                    voucherMember.setOrderType(voucherCode.getOrderType());
                    voucherMember = setVaildDay(voucherMember,voucherCode);
                    voucherCodeService.updateMemberId(voucherCode.getId(),voucherCode.getMemberId());
                    this.insert(voucherMember);
                    voucherAliService.updatePickCount(voucherCode.getVoucherId());
                    voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_REOK);
                    voucherNofity.setNotifyRemark("");
                    voucherNofityService.updateStatus(voucherNofity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_ERROR);
                    voucherNofity.setNotifyRemark(e.getMessage());
                    voucherNofityService.updateStatus(voucherNofity);
                }
            }
        }
        return null;
    }
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
                voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_NO);
                voucherNofity.setNotifyRemark("券码不存在");
                voucherNofityService.updateById(voucherNofity);
                return voucherNofity;
            }
            if(null != voucherCode.getMemberId())
            {
                voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_HAD);
                voucherNofity.setNotifyRemark("券码已被领取,member:"+voucherCode.getMemberId());
                voucherNofityService.updateById(voucherNofity);
                return voucherNofity;
            }
            member = memberService.findMemberByAliId(voucherNofity.getUid());
            if(null == member)
            {
                voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_MEMBER);
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
            voucherMember.setVoucherCount(voucherCode.getVoucherCount());
            voucherMember.setOrderType(voucherCode.getOrderType());
            voucherMember = setVaildDay(voucherMember,voucherCode);
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
    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[设置有效期]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return VoucherMember
     */
    @SuppressWarnings("static-access")
    private VoucherMember setVaildDay(VoucherMember voucherMember, VoucherCode voucherCode)
    {
        if(VoucherConst.VOUCHER_VALIDTYPE_RELATIVE.equals(voucherCode.getValidType()))
        {
            Calendar calendar  =   Calendar.getInstance();
            calendar.setTime(voucherMember.getCreateDate());
            calendar.add(Calendar.DATE, voucherCode.getValidDay());
            voucherMember.setValidStart(voucherMember.getCreateDate());
            voucherMember.setValidEnd(calendar.getTime());
        }
        else
        {
            voucherMember.setValidStart(voucherCode.getValidStart());
            voucherMember.setValidEnd(voucherCode.getValidEnd());
        }
        return voucherMember;
    }

    /**
     * <p>Created on 2019年10月27日</p>
     * <p>Description:[券的使用]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return
     */
    @Override
    public String voucherUse(VoucherBean voucherBean)
    {
        VoucherMember voucherMember = null;
        try
        {
            voucherMember = this.selectById(voucherBean.getVoucherMemberId());
            if(null == voucherMember)
            {
                return "券不存在！";
            }
            if("USED".equals(voucherMember.getVoucherStatus()))
            {
                return "券已使用！";
            }
            // 改会员券状态
            voucherMember.setOrderId(voucherBean.getOrderId());
            voucherMember.setOrderNo(voucherBean.getOrderNo());
            voucherMember.setVoucherStatus(VoucherConst.VOUCHER_STATUS_USED);
            this.updateById(voucherMember);
            // 改核销数
            voucherAliService.updatePickCount(voucherMember.getVoucherId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "用券失败！";
        }
        // 告诉支付宝
        try
        {
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format,
                    AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
            AlipayMarketingVoucherStockUseRequest request = new AlipayMarketingVoucherStockUseRequest();
            AlipayMarketingVoucherStockUseModel model = new AlipayMarketingVoucherStockUseModel();
            model.setEntityNo(voucherMember.getVoucherCode());
            model.setOutBizNo(UUID.randomUUID().toString().replaceAll("-", ""));
            request.setBizModel(model);
            AlipayMarketingVoucherStockUseResponse response = alipayClient.execute(request);
            if(response.isSuccess())
            {
                voucherMember.setAliVoucherId(response.getVoucherId());
                this.updateById(voucherMember);
            }
        }
        catch (AlipayApiException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[下单选择券]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return
     */
    @Override
    public List<VoucherMember> getVoucherForOrder(Long memberId)
    {
        return voucherMemberMapper.getVoucherForOrder(memberId);
    }
    @Override
    @Transactional
    public String updateOrderNo(BigDecimal price, Integer orderId, String voucherId, Payment payment) {

        Order order = orderService.selectById(orderId);
        BigDecimal discountPrice = voucherAliService.getDiscountPriceByVoucherId(price, voucherId);
         payment.setPrice(discountPrice);
        //判断优惠又的价格是否和原价相等，如果不一样即说明优惠了
        if (!(price.compareTo(discountPrice)== 0)){
            //将券进行绑定
            //this.updateVoucherUseing(order.getId(),order.getOrderNo(),order.getAliUserId(),Long.parseLong(voucherId));
            VoucherMember voucherMember = this.selectById(voucherId);
            voucherMember.setOrderId(order.getId());
            voucherMember.setOrderNo(order.getOrderNo());
            this.updateById(voucherMember);
        }
        return  paymentService.genalPayXcx(payment,order);
    }
    /**
     * 更新券为使用中的状态（即绑定状态）
     * @param orderId
     * @param orderNo
     * @param voucherMemberId
     * @return
     */
    @Override
    @Transactional
    public boolean updateVoucherUseing(long orderId,String orderNo,String aliUserId,long voucherMemberId){
        boolean bool = false;
        VoucherMember voucherMember = this.selectById(voucherMemberId);
        if (null!= voucherMember&& VoucherConst.VOUCHER_STATUS_CREATE.equals(voucherMember.getVoucherStatus())&&voucherMember.getAliUserId().equals(aliUserId)){
            voucherMember.setOrderId(orderId);
            voucherMember.setOrderNo(orderNo);
            voucherMember.setVoucherStatus(VoucherConst.VOUCHER_STATUS_USEING);
            bool = this.updateById(voucherMember);
        }
        return  bool;
    }

    /**
     * 更新券为可使用的状态（即领取待使用状态）
     * @param voucherMemberId
     * @return
     */
    @Override
    @Transactional
    public boolean updateVoucherCreate(long voucherMemberId){
        boolean bool = false;
        VoucherMember voucherMember = this.selectById(voucherMemberId);
        if (null!= voucherMember&& VoucherConst.VOUCHER_STATUS_USEING.equals(voucherMember.getVoucherStatus())){
            voucherMember.setOrderId((long)0);
            voucherMember.setOrderNo("");
            voucherMember.setVoucherStatus(VoucherConst.VOUCHER_STATUS_CREATE);
            bool = this.updateById(voucherMember);
        }
        return  bool;
    }
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
        wrapper.eq("voucher_status",VoucherConst.VOUCHER_STATUS_CREATE);
        wrapper.eq("voucher_type","REVIVE");
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

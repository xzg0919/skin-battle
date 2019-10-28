package com.tzj.collect.core.service.impl;

import static com.tzj.collect.common.constant.Const.ALI_PAY_KEY;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMarketingVoucherStockUseRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayMarketingVoucherStockUseResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.Const;
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
        if("relative".equals(voucherCode.getValidType()))
        {
            Calendar calendar  =   Calendar.getInstance();
            calendar.setTime(voucherMember.getCreateDate()); 
            calendar.add(calendar.DATE, voucherCode.getValidDay());
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
            voucherMember.setVoucherStatus("USED");
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
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.appId, AlipayConst.private_key, AlipayConst.format, 
                    AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingVoucherStockUseRequest request = new AlipayMarketingVoucherStockUseRequest();
            String bizContent = "";
            bizContent = bizContent
                       + "{" 
                       + "\"entity_no\":\""
                       + voucherMember.getVoucherCode()
                       + "\"," 
                       + "\"out_biz_no\":\""
                       + UUID.randomUUID().toString().replaceAll("-", "")
                       + "}";
            request.setBizContent(bizContent);
            AlipayMarketingVoucherStockUseResponse response = alipayClient.execute(request);
            if(!response.isSuccess())
            {
            
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
    public String updateOrderNo(BigDecimal price, Integer orderId, String voucherId, Payment payment) {

        Order order = orderService.selectById(orderId);
        VoucherMember voucherMember = this.selectOne(new EntityWrapper<VoucherMember>().eq("ali_user_id", order.getAliUserId()).eq("voucher_id", voucherId).eq("voucher_status","CREATE"));
        if (null != voucherMember){
            voucherMember.setOrderId(order.getId());
            voucherMember.setOrderNo(order.getOrderNo());
            voucherMember.setVoucherStatus("USEING");
            this.updateById(voucherMember);
            BigDecimal discountPrice = voucherAliService.getDiscountPriceByVoucherId(price, voucherId);
            payment.setPrice(discountPrice);
        }
        order.setDiscountPrice(payment.getPrice());
        orderService.updateById(order);
        return  paymentService.genalPayXcx(payment);
    }

//    public static void main(String[] args) {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
//        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
//        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
//        String sn = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
//        String subject = "垃圾分类回收订单(收呗):" + sn;
//
//        model.setBody(subject);
//        model.setSubject(subject);
//        model.setOutTradeNo(sn);
//        model.setTimeoutExpress("1m");
//        model.setBuyerId("2088212854989662");
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088421446748174");
//        model.setExtendParams(extendParams);
//        model.setTotalAmount("0.1");
//        request.setBizModel(model);
//        request.setNotifyUrl("http://shoubeics.mayishoubei.com/notify/alipay");
//
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecutee
//            AlipayTradeCreateResponse response = alipayClient.execute(request);
//            System.out.println(response.getTradeNo());
//        } catch (AlipayApiException e) {
//            throw new ApiException("系统异常：" + e.getErrMsg());
//        }
//    }

    public static void main(String[] args)throws Exception{
        String sn = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", Const.ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", Const.ALI_PUBLIC_KEY, "RSA2");
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizContent("{" +
                "\"trade_no\":\"2019102722001489665709551035\"," +
                "\"operator_id\":\""+sn+"\"" +
                "  }");
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
    
}

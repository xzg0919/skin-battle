package com.tzj.collect.core.service.impl;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.constant.WXConst;
import com.tzj.collect.core.mapper.WxPaymentMapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WxPaymentServiceImpl extends ServiceImpl<WxPaymentMapper, WxPayment> implements WxPaymentService {

    @Autowired
    OrderService orderService;
    @Autowired
    PaymentErrorService paymentErrorService;
    @Autowired
    RecyclersService recyclersService;
    @Autowired
    WXPayService wxPayService;

    @Override
    public WxPayment findByOutTradeNo(String outTradeNo) {
        return this.selectOne(new EntityWrapper<WxPayment>().eq("out_trade_no", outTradeNo));
    }


    /**
     * 微信转账
     *
     * @param payment
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(WxPayment payment) {
        Order order = orderService.selectOne(new EntityWrapper<Order>().eq("order_no", payment.getOrderNo()));
        OrderBean orderBean = new OrderBean();
        orderBean.setDiscountPrice(payment.getDiscountPrice().toString());
        String openId = "";
        if ((order.getTitle() + "").equals(Order.TitleType.BIGTHING + "")) {
            Recyclers recyclers = recyclersService.selectById(order.getRecyclerId());
            openId = recyclers.getOpenId();
        } else {
            openId = payment.getOpenId();
        }
        wxPayService.transfer(payment.getOrderNo(), openId, payment.getTransferPrice(), "垃圾分类回收(收呗)货款", WXConst.FROM_APP);
        //修改订单状态
        orderBean.setId(order.getId().intValue());
        orderBean.setStatus("3");
        orderBean.setAmount(order.getGreenCount());
        orderService.modifyOrderByPayment(orderBean, payment.getVoucherMember());
    }

}

package com.tzj.collect.core.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayCommerceIndustryOrderSyncRequest;
import com.alipay.api.response.AlipayCommerceIndustryOrderSyncResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.param.sync.*;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.OrderSyncService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Order;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.tzj.collect.core.param.sync.OrderSyncBizContent.HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE;

/**
 * @Auther: Administrator
 * @Date: 2022/03/28 0028 12:03
 * @Description:
 */
@Slf4j
@Service
public class OrderDigitalSyncService  implements OrderSyncService {


    @Autowired
    CategoryService categoryService;

    @Override
    public  String OrderSync(Order order, String status){

        AlipayClient alipayClient = new DefaultAlipayClient
                ("https://openapi.alipay.com/gateway.do",
                        AlipayConst.XappId, AlipayConst.private_key, "json",
                        "GBK", AlipayConst.ali_public_key, "RSA2");
        Category category = categoryService.selectById(order.getCategoryId());

            Category  parentCategory = categoryService.selectById(category.getParentId());

        AlipayCommerceIndustryOrderSyncRequest request = new AlipayCommerceIndustryOrderSyncRequest();
        OrderSyncBizContent orderSyncBizContent=new OrderSyncBizContent();
        orderSyncBizContent.setMerchant_order_no(order.getOrderNo());
        orderSyncBizContent.setService_type("HOUSEHOLD_APPLIANCES_RECYCLING");
        orderSyncBizContent.setBuyer_id(order.getAliUserId());
        orderSyncBizContent.setService_code(HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE);
        orderSyncBizContent.setStatus(status);
        orderSyncBizContent.setOrder_create_time(DateUtils.formatDate(order.getCreateDate()==null?new Date():order.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
        orderSyncBizContent.setOrder_modify_time(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        String orderId ="10086";
        if(order.getId()!=null && order.getId() != 0L){
            orderId  = order.getId()+"";
        }
        orderSyncBizContent.setOrder_detail_url("alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/orderDetails/appliances/appliances%3Fid%3D"+orderId);
        orderSyncBizContent.setOrder_amount(order.getPrice()==null?"0":order.getPrice().toString());
        orderSyncBizContent.setPayment_amount(order.getAchPrice()==null?"0":order.getAchPrice().toString());

        IndustryInfo industryInfo=new IndustryInfo();
        ServiceProductInfo serviceProductInfo=new ServiceProductInfo();
        serviceProductInfo.setGoods_desc("家电回收");
        serviceProductInfo.setGoods_name(parentCategory==null?"家电":parentCategory.getName());
        serviceProductInfo.setQuantity("1");
        industryInfo.setService_product_info(serviceProductInfo);

        ServicePerformanceInfo servicePerformanceInfo =new ServicePerformanceInfo();
        AppointmentTime appointmentTime =new AppointmentTime();
        if(order.getArrivalPeriod().equals("am")){
            appointmentTime.setStart_time( DateUtils.formatDate(order.getArrivalTime(),"yyyy-MM-dd")+" 09:00:00");
            appointmentTime.setEnd_time(DateUtils.formatDate(order.getArrivalTime(),"yyyy-MM-dd")+" 12:00:00");
        }else{
            appointmentTime.setStart_time(DateUtils.formatDate(order.getArrivalTime(),"yyyy-MM-dd")+" 13:00:00");
            appointmentTime.setEnd_time(DateUtils.formatDate(order.getArrivalTime(),"yyyy-MM-dd")+" 18:00:00");
        }

        servicePerformanceInfo.setAppointment_time(appointmentTime);
        industryInfo.setService_performance_info(servicePerformanceInfo);
        orderSyncBizContent.setIndustry_info(industryInfo);
        request.setBizContent(com.alibaba.fastjson.JSONObject.toJSONString(orderSyncBizContent));
        log.info("家电订单回流请求参数："+com.alibaba.fastjson.JSONObject.toJSONString(orderSyncBizContent));
        AlipayCommerceIndustryOrderSyncResponse response = null;
        try {
            response = alipayClient.execute(request,order.getAccessToken());
        } catch (AlipayApiException e) {
            e.printStackTrace();

        }
        log.info("家电订单回流返回参数："+com.alibaba.fastjson.JSONObject.toJSONString(response));
        if(!response.isSuccess() || !"10000".equals(response.getCode()) || (StringUtils.isBlank(response.getRecordId()) && !status.equals("CANCELED"))){
            throw new ApiException("操作失败，订单无法回流！");
        }
        return response.getRecordId();
    }



    @Override
    public   String orderSyncCreate(Order order){
        return OrderSync(order,"CREATE");

    }
    @Override
    public   String orderSyncToSend(Order order){
        return OrderSync(order,"TO_SEND");

    }
    @Override
    public   String orderSyncTaken(Order order){
        return OrderSync(order,"ORDER_TAKEN");

    }

    @Override
    public   String orderSyncAccount(Order order){
        return OrderSync(order,"ACCOUNT");

    }
    @Override
    public   String orderSyncCanceled(Order order){
        return OrderSync(order,"CANCELED");

    }
}

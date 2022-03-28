package com.tzj.collect.core.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayCommerceIndustryOrderSyncRequest;
import com.alipay.api.response.AlipayCommerceIndustryOrderSyncResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.param.sync.*;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Order;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @Auther: Administrator
 * @Date: 2022/03/25 0025 14:05
 * @Description:
 */
public interface OrderSyncService {


      String OrderSync(Order order, String status);



        String orderSyncCreate(Order order);


        String orderSyncToSend(Order order);


        String orderSyncTaken(Order order);


        String orderSyncAccount(Order order);

        String orderSyncCanceled(Order order);
}

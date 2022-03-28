package com.tzj.collect.core.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayCommerceIndustryOrderSyncRequest;
import com.alipay.api.response.AlipayCommerceIndustryOrderSyncResponse;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.param.sync.*;
import com.tzj.collect.core.service.OrderSyncService;
import com.tzj.collect.core.service.impl.OrderClothesSyncService;
import com.tzj.collect.core.service.impl.OrderDigitalSyncService;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Order;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.tzj.collect.core.param.sync.OrderSyncBizContent.HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE;

/**
 * @Auther: Administrator
 * @Date: 2022/03/25 0025 14:05
 * @Description:
 */
public class OrderSyncFactory {



   public  enum OrderTitle{
        DIGITAL,CLOTHES
    }


    public static OrderSyncService instance(OrderTitle orderTitle){

        if(orderTitle.equals(OrderTitle.DIGITAL)){
            return  new OrderDigitalSyncService();
        } else if (orderTitle.equals(OrderTitle.CLOTHES)){
            return  new OrderClothesSyncService();
        }
        return null;

    }

}

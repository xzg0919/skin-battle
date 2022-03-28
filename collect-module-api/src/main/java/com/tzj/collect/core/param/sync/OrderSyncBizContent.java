package com.tzj.collect.core.param.sync;

import lombok.Data;

@Data
   public  class OrderSyncBizContent{
     /**
      * 家电回收
      */
     public static final String HOUSEHOLD_ELECTRICAL_APPLIANCES_RECYCLE = "2022032421000650389087";

     /**
      * 衣物回收
      */
     public static final String CLOTHES_RECYCLE = "2022032521000250660631";


        String merchant_order_no;
        String service_type;
        String buyer_id;
        String service_code ;
        String order_source ="ALIPAY_APPLETS";
        String status;
        String order_create_time;
        String order_modify_time;
        String order_detail_url;
        String order_amount;
        String payment_amount;
        IndustryInfo industry_info;


        String record_id ;
    }
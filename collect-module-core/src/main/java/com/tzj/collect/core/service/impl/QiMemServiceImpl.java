package com.tzj.collect.core.service.impl;


import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaIdleRecycleOrderFulfillmentRequest;
import com.taobao.api.response.AlibabaIdleRecycleOrderFulfillmentResponse;
import com.tzj.collect.common.util.TaobaoUtil;
import com.tzj.collect.core.param.xianyu.QiMemOrder;
import com.tzj.collect.core.service.QiMemService;
import org.springframework.stereotype.Service;


@Service
public class QiMemServiceImpl implements QiMemService {




        @Override
        public  boolean updateQiMemOrder(QiMemOrder qiMemOrder){
            TaobaoClient client = new DefaultTaobaoClient(TaobaoUtil.TAOBAO_APP_URL, TaobaoUtil.TAOBAO_APP_KEY, TaobaoUtil.TAOBAO_APP_SECRET);
            AlibabaIdleRecycleOrderFulfillmentRequest req = new AlibabaIdleRecycleOrderFulfillmentRequest();
            AlibabaIdleRecycleOrderFulfillmentRequest.RecycleOrderSynDto obj1 = new AlibabaIdleRecycleOrderFulfillmentRequest.RecycleOrderSynDto();
            AlibabaIdleRecycleOrderFulfillmentRequest.Attribute obj2 = new AlibabaIdleRecycleOrderFulfillmentRequest.Attribute();
            if ("103".equals(qiMemOrder.getOrderStatus())){
                obj2.setReason(qiMemOrder.getReason());//103必传
            }else if ("5".equals(qiMemOrder.getOrderStatus())){
                obj2.setConfirmFee(qiMemOrder.getConfirmFee()); //5必传
                obj2.setQuantity(qiMemOrder.getQuantity());//5必传
            }
            obj1.setAttribute(obj2);
            obj1.setBizOrderId(qiMemOrder.getBizOrderId());
            obj1.setOrderStatus(qiMemOrder.getOrderStatus());//5完成  103回收商取消订单
            obj1.setPartnerKey(TaobaoUtil.TAOBAO_APP_KEY);
            req.setParam0(obj1);
            AlibabaIdleRecycleOrderFulfillmentResponse response = null;
            try {
                response = client.execute(req);
                System.out.println("通知咸鱼订单变更状态："+ qiMemOrder.getOrderStatus()+"   变更状态："+response.isSuccess());
            }catch (Exception e){
                e.printStackTrace();
            }
            return response.isSuccess();
        }
//
//    public static void main(String[] args) {
//        TaobaoClient client = new DefaultTaobaoClient(TaobaoUtil.TAOBAO_APP_URL, TaobaoUtil.TAOBAO_APP_KEY, TaobaoUtil.TAOBAO_APP_SECRET);
//        UserBuyerGetRequest req = new UserBuyerGetRequest();
//        req.setFields("nick,sex");
//        UserBuyerGetResponse rsp = client.execute(req, sessionKey);
//        System.out.println(rsp.getBody());
//    }




}

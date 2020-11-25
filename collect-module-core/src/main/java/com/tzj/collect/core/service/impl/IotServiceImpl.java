package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AntMerchantExpandTradeorderSyncResponse;
import com.tzj.collect.core.param.iot.IOT4OrderBean;
import com.tzj.collect.core.param.iot.IotOrderVo;
import com.tzj.collect.core.param.mysl.MyslBean;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.IotCompanyCategory;
import com.tzj.collect.entity.IotOrder;
import com.tzj.collect.entity.IotOrderDetail;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
@Transactional(readOnly = true)
public class IotServiceImpl implements IotService {

    @Autowired
    IotOrderService iotOrderService;
    @Autowired
    IotOrderDetailService iotOrderDetailService;
    @Autowired
    AnsycMyslService ansycMyslService;
    @Autowired
    OrderService orderService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveOrder(IOT4OrderBean iot4OrderBean,
                            String deviceAddress, String companyName, Long companyId,
                            IotCompanyCategory iotCompanyCategory, String aliUserId) {
        String orderNo = "";
        //如果orderNo为空，创建订单
        if (StringUtils.isBlank(iot4OrderBean.getOrderNo())) {
            orderNo = "IOT" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
            //生成订单
            IotOrder iotOrder = new IotOrder(orderNo, aliUserId, iot4OrderBean.getDeviceCode(),
                    deviceAddress, companyId, companyName);
            iotOrderService.insert(iotOrder);

            //生成订单明细
            IotOrderDetail iotOrderDetail = new IotOrderDetail(iotOrder.getId(), iotCompanyCategory.getName(),
                    iotCompanyCategory.getCategoryId(), iotCompanyCategory.getUnit(), iot4OrderBean.getWeight(),
                    iotCompanyCategory.getUnitPoint(), iotCompanyCategory.getUnitPrice());
            iotOrderDetailService.insert(iotOrderDetail);


        } else {
            //生成订单
            IotOrder iotOrder = iotOrderService.selectOrderByOrderNo(iot4OrderBean.getOrderNo(), IotOrder.OrderStatus.PROCESSING);
            if (iotOrder == null) {
                throw new ApiException("暂无进行中的订单");
            }
            orderNo = iotOrder.getOrderNo();
            //判断该品类是否是多次添加 ，否则新建
            IotOrderDetail iotOrderDetail = iotOrderDetailService.selectByOrderIdAndCategoryId(iotOrder.getId(), iotCompanyCategory.getCategoryId());
            if (iotOrderDetail == null) {
                iotOrderDetail = new IotOrderDetail(iotOrder.getId(), iotCompanyCategory.getName(),
                        iotCompanyCategory.getCategoryId(), iotCompanyCategory.getUnit(), iot4OrderBean.getWeight(), iotCompanyCategory.getUnitPoint(), iotCompanyCategory.getUnitPrice());
            } else {
                iotOrderDetail.setUnitAmount(iotOrderDetail.getUnitAmount() + iot4OrderBean.getWeight());
            }
            //生成订单明细
            iotOrderDetailService.insertOrUpdate(iotOrderDetail);
        }

        return orderNo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IotOrderVo completeOrder(String orderNo, int orderType) {

        //查找订单
        IotOrder order = iotOrderService.selectOrderByOrderNo(orderNo, IotOrder.OrderStatus.PROCESSING);
        if (order == null) {
            throw new ApiException("订单不存在");
        }
        List<IotOrderVo.IotOrderDetail> iotOrderVoDetails = new ArrayList<>();

        //发放蚂蚁森林
        if (orderType == 1) {
            try {
                MyslBean myslBean = new MyslBean();
                myslBean.setAliUserId(order.getAliUserId());
                myslBean.setOutBizNo(order.getOrderNo());

                //获取蚂蚁森林的品类
                List<MyslItemBean> myslItemBeans = iotOrderDetailService.findMyslParamsByOrderId(order.getId());
                myslBean.setMyslItemBeans(myslItemBeans);

                if (myslItemBeans == null || myslItemBeans.size() == 0) {
                    throw new ApiException("订单不存在");
                }

                myslItemBeans.forEach(myslItemBean -> {
                    IotOrderVo.IotOrderDetail iotOrderDetail = new IotOrderVo().new IotOrderDetail();
                    iotOrderDetail.setWeight(new BigDecimal(myslItemBean.getCount().toString()).divide(new BigDecimal("1000")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                    iotOrderDetail.setCategoryName(myslItemBean.getItemName());
                    iotOrderVoDetails.add(iotOrderDetail);
                });

                order.setOrderType(IotOrder.OrderType.MYSL);
                AntMerchantExpandTradeorderSyncResponse antMerchantExpandTradeorderSyncResponse = ansycMyslService.updateCansForestByList(myslBean);
                if (antMerchantExpandTradeorderSyncResponse.isSuccess()) {
                    order.setMyslOrderId(antMerchantExpandTradeorderSyncResponse.getOrderId());
                    order.setMyslParam(JSONObject.toJSON(antMerchantExpandTradeorderSyncResponse.getParams()).toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.error("定时定点发放蚂蚁森林能量失败--------订单id：" + order.getOrderNo() + "+异常信息：" + e.getMessage());

            }
        }

        //总积分值
        BigDecimal point = new BigDecimal("0.0");


        //发放积分
        if (orderType == 0) {
            List<IotOrderDetail> iotOrderDetails = iotOrderDetailService.selectByOrderId(order.getId());
            if (iotOrderDetails == null || iotOrderDetails.size() == 0) {
                throw new ApiException("订单不存在");
            }
            for (IotOrderDetail iotOrderDetail : iotOrderDetails) {
                iotOrderDetail.setAmount(new BigDecimal(String.valueOf(iotOrderDetail.getUnitAmount()))
                        .multiply(new BigDecimal(String.valueOf(iotOrderDetail.getUnitPoint()))).setScale(2, BigDecimal.ROUND_DOWN));
                point = point.add(iotOrderDetail.getAmount());

                IotOrderVo.IotOrderDetail iotOrderDetailVo = new IotOrderVo().new IotOrderDetail();
                iotOrderDetailVo.setAmount(iotOrderDetail.getAmount());
                iotOrderDetailVo.setCategoryName(iotOrderDetail.getName());
                iotOrderDetailVo.setWeight(iotOrderDetail.getUnitAmount());
                iotOrderVoDetails.add(iotOrderDetailVo);
            }
            iotOrderDetailService.updateBatchById(iotOrderDetails);
            order.setOrderType(IotOrder.OrderType.POINT);

            orderService.updateMemberPoint(order.getAliUserId(), order.getOrderNo(), point.doubleValue(), "IOT订单加积分");
        }
        order.setStatus(IotOrder.OrderStatus.COMPLETE);
        order.setAmount(point);
        iotOrderService.updateById(order);


        IotOrderVo iotOrderVo = new IotOrderVo();
        iotOrderVo.setOrderNo(order.getOrderNo());
        iotOrderVo.setOrderType((Integer) order.getOrderType().getValue());
        iotOrderVo.setIotOrderDetail(iotOrderVoDetails);
        return iotOrderVo;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelOrder(String orderNo) {

        //查找订单
        IotOrder order = iotOrderService.selectOrderByOrderNo(orderNo, IotOrder.OrderStatus.PROCESSING);
        if (order == null) {
            throw new ApiException("订单不存在");
        }
        order.setStatus(IotOrder.OrderStatus.CANCEL);
        iotOrderService.updateById(order);
    }


}

package com.tzj.collect.core.service;


import com.tzj.collect.core.param.iot.IOT4OrderBean;
import com.tzj.collect.core.param.iot.IotOrderVo;
import com.tzj.collect.entity.IotCompanyCategory;

public interface IotService {


    /**
     * 保存订单信息
     * @param iot4OrderBean  传入的bean
     * @param deviceAddress  IOT设备地址
     * @param companyName   公司名称
     * @param companyId   公司id
     * @param iotCompanyCategory  公司品类
     * @param aliUserId  用户id
     * @return
     */
    String saveOrder(IOT4OrderBean iot4OrderBean, String deviceAddress, String companyName,
                     Long companyId, IotCompanyCategory iotCompanyCategory,String aliUserId);


    /**
     * 完成订单
     * @param orderNo  订单号
     * @param orderType  完成订单类型  0:积分  1:森林能量
     */
    IotOrderVo completeOrder(String orderNo,int orderType);

    /**
     * 取消订单
     * @param orderNo 订单编号
     */
    void cancelOrder(String orderNo);




}

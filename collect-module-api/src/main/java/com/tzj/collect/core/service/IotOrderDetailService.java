package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.mysl.MyslItemBean;
import com.tzj.collect.entity.IotOrder;
import com.tzj.collect.entity.IotOrderDetail;

import java.util.List;
import java.util.Map;


public interface IotOrderDetailService extends IService<IotOrderDetail>{

    /**
     * 通过订单ID获取订单明细
     * @param orderId
     * @return
     */
    List<IotOrderDetail> selectByOrderId(Long orderId);

    /**
     * orderId获取发放蚂蚁森林的参数
     * @param orderId
     * @return
     */
    List<MyslItemBean> findMyslParamsByOrderId(Long orderId);


    IotOrderDetail selectByOrderIdAndCategoryId(Long orderId,Long categoryId);


    List<Map<String,Object>> selectDetailByOrderId(Long orderId);




}

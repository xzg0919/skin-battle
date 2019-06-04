package com.tzj.collect.api.app;


import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

import java.util.Map;

import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.OrderPicAchService;
import com.tzj.collect.service.OrderService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 大件订单信息
 */
@ApiService
public class AppBigOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderPicAchService orderPicAchService;

    /** 根据手机号搜索
      * @author sgmark@aliyun.com
      * @date 2019/6/4 0004
      * @param
      * @return
      */
    @Api(name = "app.big.order.list.phone", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public Map<String,Object> getOrderListByPhone(OrderBean orderbean){
        orderbean.setRecyclerId(Integer.valueOf(RecyclersUtils.getRecycler().getId().toString()));
        Map<String,Object> pageOrder = orderService.getBigOrderListByPhone(orderbean);
        return pageOrder;
    }


    /**
     * 根据订单传来的状态获取订单列表
     */
    @Api(name = "app.bigOrder.getBigOrderList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public Map<String,Object> getBigOrderList(OrderBean orderBean){
        Recyclers recycler = RecyclersUtils.getRecycler();
        Map<String,Object> bigOrderList = orderService.getBigOrderList(orderBean.getStatus(),recycler.getId(),orderBean.getPagebean());
        return bigOrderList;
    }
    /**
     * 根据订单id查询订单的详细信息
     * */
    @Api(name = "app.bigOrder.getBigOrderDetails", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public Map<String,Object> getBigOrderDetails(OrderBean orderBean){
        Recyclers recycler = RecyclersUtils.getRecycler();
        Map<String,Object> bigOrderList = orderService.getBigOrderDetails(orderBean.getId());
        return bigOrderList;
    }

    /**
     * 根据订单id保存图片、签名和回收物描述
     * */
    @Api(name = "app.bigOrder.setAchOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String setAchOrder(OrderBean orderBean){
        return orderService.setAchOrder(orderBean);
    }


    /**
     * 根据订单id保存价格
     * */
    @Api(name = "app.bigOrder.saveBigOrderPrice", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String saveBigOrderPrice(OrderBean orderBean){
        return orderService.saveBigOrderPrice(orderBean);
    }

    /**
     * 根据订单id保存订单备注
     * */
    @Api(name = "app.bigOrder.saveBigOrderRemarks", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String saveBigOrderRemarks(OrderBean orderBean){
        Order order = orderService.selectById(orderBean.getId());
        order.setOrderRemarks(orderBean.getOrderRemarks());
        orderService.updateById(order);
        return "操作成功";
    }

    /**
     * 根据订单id保存订单备注
     * */
    @Api(name = "app.bigOrder.deleteBigOrderRemarks", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
    public String deleteBigOrderRemarks(OrderBean orderBean){
        orderService.deleteBigOrderRemarks(orderBean.getId());
        return "操作成功";
    }






}

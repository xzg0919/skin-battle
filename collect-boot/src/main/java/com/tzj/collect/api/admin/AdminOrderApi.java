package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.service.OrderCancleExamineService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import com.tzj.collect.entity.OrderStatusType;
import com.tzj.collect.entity.OrdersType;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class AdminOrderApi {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderCancleExamineService orderCancleExamineService;

    /**
     * 根据条件获取订单内容
     * @param
     * @return
     */
    @Api(name = "admin.getOrderList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderList(OrderBean orderBean){
        return orderService.getOrderListByAdmin(orderBean);
    }
    /**
     * 获取订单类型列表
     * @param
     * @return
     */
    @Api(name = "admin.getOrderTitle", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderTitle(){
        return OrdersType.typeMapList();
    }

    /**
     * 获取订单状态列表
     * @param
     * @return
     */
    @Api(name = "admin.getOrderStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderStatus(){
        return OrderStatusType.typeMapList();
    }

    /**
     * 根据订单Id获取订单详情
     * @param
     * @return
     */
    @Api(name = "admin.getOrderDetailById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderDetailById(OrderBean orderBean){
        return orderService.getOrderDetailByIdByAdmin(orderBean.getId().toString());
    }
    /**
     * 中台驳回接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.updateOdrerStatusByAdmin", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String updateOdrerStatusByAdmin(BOrderBean orderbean) {
        //订单id
        Integer orderId = orderbean.getId();
        //驳回原因
        String cancelReason = orderbean.getCancelReason();
        String sta = orderService.updateOrderByBusiness(orderId,"REJECTED",cancelReason,null);
        return sta;
    }
    /**
     * 申请取消列表接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.getOdrerCancleExamineList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOdrerCancleExamineList(OrderBean orderbean) {
            return orderService.getOrderCancleExamineList(orderbean);
    }
    /**
     * 订单详情接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.getAdminOrderDetail", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getNewOrderDetail(OrderBean orderbean) {
        Map<String,Object> resultMap = (Map<String, Object>) orderService.getAdminOrderDetail(orderbean.getId());
        Order order = (Order) resultMap.get("order");
        OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));
        resultMap.put("orderCancleExamine",orderCancleExamine);
        return resultMap;
    }
    /**
     * 中台驳回接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.agreeExamineOdrerStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String agreeExamineOdrerStatus(OrderBean orderbean) {
       return orderService.agreeExamineOdrerStatus(orderbean);
    }

}

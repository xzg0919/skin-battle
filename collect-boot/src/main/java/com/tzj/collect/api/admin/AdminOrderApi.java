package com.tzj.collect.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.ArrivalTimeLogBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import com.tzj.collect.entity.OrderStatusType;
import com.tzj.collect.entity.OrdersType;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@ApiService
public class AdminOrderApi {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderCancleExamineService orderCancleExamineService;
    @Autowired
    private ArrivalTimeLogService arrivalTimeLogService;
    @Resource(name = "mqtt4PushOrder")
    private MqttClient mqtt4PushOrder;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;

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
     * 根据订单id获取订单详情
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.getOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> getAdminOrderDetail(OrderBean orderBean){
        int orderId = orderBean.getId();
        //查询订单详情
        Map<String,Object> resultMap = orderService.selectOrderByBusiness(orderId);
        return resultMap;
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
        String sta = orderService.updateOrderByBusiness(orderId,"REJECTED",cancelReason,null,mqtt4PushOrder);
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
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getNewOrderDetail(OrderBean orderbean) {
        Map<String,Object> resultMap = (Map<String, Object>) orderService.getAdminOrderDetail(orderbean.getId());
        Order order = (Order) resultMap.get("order");
        OrderCancleExamine orderCancleExamine = orderCancleExamineService.selectOne(new EntityWrapper<OrderCancleExamine>().eq("order_no", order.getOrderNo()));
        resultMap.put("orderCancleExamine",orderCancleExamine);
        return resultMap;
    }
    /**
     * 根据订单id获取取消订单申请理由
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.getCancleOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getCancleOrderDetail(OrderBean orderbean){
        //查询订单详情
        return orderCancleExamineService.getCancleOrderDetail(orderbean);
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
       return orderService.agreeExamineOdrerStatus(orderbean,mqtt4PushOrder);
    }

    /**
     * 用户修改上门回收时间
     * @author wangcan
     * @return
     */
    @Api(name = "admin.sendArrivalTimeLog", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public String sendArrivalTimeLog(ArrivalTimeLogBean arrivalTimeLogBean){
        //获取订单Id
        Integer orderId =  arrivalTimeLogBean.getOrderId();
        return arrivalTimeLogService.sendArrivalTimeLog(orderId,arrivalTimeLogBean.getAfterDate(),arrivalTimeLogBean.getAfterPeriod(),arrivalTimeLogBean.getCancleDesc());

    }

    /**
     * 订单详情接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.getReceptionOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getReceptionOrderDetail(OrderBean orderbean) {
        return orderService.getAdminOrderDetail(orderbean.getId());
    }
    /**
     * 新增订单催促接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.saveOrderReceptionByOrderNo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object saveOrderReceptionByOrderNo(OrderBean orderbean) {
        return orderService.saveOrderReceptionByOrderNo(orderbean);
    }

    /**
     * 订单详情接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.getOrderDetailByOrderId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderDetailByOrderId(OrderBean orderbean) {
        return orderService.getOrderDetailByOrderId(orderbean.getId());
    }


    /**
     * 根据不查询闲鱼订单列表
     * @param
     * @return
     */
    @Api(name = "admin.order.getXyOrderListByAdminReception", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getXyOrderListByAdminReception(OrderBean orderBean){
        return orderService.getXyOrderListByAdminReception(orderBean);
    }

    /**
     * 根据父级id获取相关城市列表
     * @param
     * @return
     */
    @Api(name = "admin.area.getAreaListByParentId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getAreaListByParentId(OrderBean orderBean){
        return areaService.getAreaListByParentId(orderBean);
    }

    /**
     * 根据街道id查询相关公司
     * @param
     * @return
     */
    @Api(name = "admin.getCompanyIdByStreetId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getCompanyIdByStreetId(OrderBean orderBean){
        Integer companyId = companyStreetHouseService.selectStreetHouseCompanyId(orderBean.getStreetId());
        if (null==companyId){
            throw new ApiException("查询失败，该区域未覆盖生活垃圾");
        }
        return  companyService.selectById(companyId);
    }

    /**
     * 根据公司Id，和订单id进行派单
     * @param
     * @return
     */
    @Api(name = "admin.sendXyOrderByCompanyId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object sendXyOrderByCompanyId(OrderBean orderBean){
        return  orderService.sendXyOrderByCompanyId(orderBean);
    }


}

package com.tzj.collect.api.adminReceeption;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.ArrivalTimeLogBean;
import com.tzj.collect.core.service.ArrivalTimeLogService;
import com.tzj.collect.core.service.CompanyService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.ArrivalTimeLog;
import com.tzj.collect.entity.CompanyStree;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.common.constant.TokenConst.*;

@ApiService
public class ReceptionOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ArrivalTimeLogService arrivalTimeLogService;

    /**
     * 根据不同的条件查询订单列表
     * @param
     * @return
     */
    @Api(name = "admin.order.getOrderListByAdminReception", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object getOrderListByAdminReception(OrderBean orderBean){
        return orderService.getOrderListByAdminReception(orderBean);
    }

    /**
     * 获取各大公司的列表
     * @param
     * @return
     */
    @Api(name = "admin.company.getCompanyList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object getCompanyList(CompanyBean companyBean){
        return companyService.getCompanyList(companyBean.getCompanyName());
    }
    /**
     * 订单详情接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.getReceptionOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object getReceptionOrderDetail(OrderBean orderbean) {
        return orderService.getAdminOrderDetail(orderbean.getId());
    }
    /**
     * 新增订单催促接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "admin.order.saveOrderReceptionByOrderNo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object saveOrderReceptionByOrderNo(OrderBean orderbean) {
        return orderService.saveOrderReceptionByOrderNo(orderbean);
    }

    /**
     * 用户修改上门回收时间
     * @author wangcan
     * @return
     */
    @Api(name = "admin.order.sendArrivalTimeLog", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
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
    @Api(name = "reception.getOrderDetailByOrderId", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object getOrderDetailByOrderId(OrderBean orderbean) {
        return orderService.getOrderDetailByOrderId(orderbean.getId());
    }
    /**
     * 根据订单id获取订单详情
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "reception.getOrderDetail", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Map<String,Object> getOrderDetail(OrderBean orderBean){
        int orderId = orderBean.getId();
        //查询订单详情
        Map<String,Object> resultMap = orderService.selectOrderByBusiness(orderId);
        return resultMap;
    }

}

package com.tzj.collect.api.adminReceeption;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.CompanyService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.CompanyStree;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderCancleExamine;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.ADMIN_RECEPTION_API_COMMON_AUTHORITY;

@ApiService
public class ReceptionOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyService companyService;

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
}

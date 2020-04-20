package com.tzj.collect.api.adminReceeption;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.ArrivalTimeLogBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@ApiService
public class ReceptionOrderApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ArrivalTimeLogService arrivalTimeLogService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CompanyStreetHouseService companyStreetHouseService;

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

    /**
     * 根据不查询闲鱼订单列表
     * @param
     * @return
     */
    @Api(name = "admin.order.getXyOrderListByAdminReception", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
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
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
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
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
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
    @RequiresPermissions(values = ADMIN_RECEPTION_API_COMMON_AUTHORITY)
    public Object sendXyOrderByCompanyId(OrderBean orderBean){
        return  orderService.sendXyOrderByCompanyId(orderBean);
    }
}

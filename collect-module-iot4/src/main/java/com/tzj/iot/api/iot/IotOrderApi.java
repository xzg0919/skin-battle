package com.tzj.iot.api.iot;

import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.iot.IOT4OrderBean;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.CompanyService;
import com.tzj.collect.core.service.IotCompanyCategoryService;
import com.tzj.collect.core.service.IotService;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;


import static com.tzj.collect.common.constant.TokenConst.EQUIPMENT_APP_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2020/11/20 14:39
 * @Description: IOT订单Api
 */

@ApiService
public class IotOrderApi {
    @Autowired
    CompanyEquipmentService companyEquipmentService;
    @Autowired
    CompanyService companyService;
    @Autowired
    IotCompanyCategoryService iotCompanyCategoryService;
    @Autowired
    IotService iotService;


    /**
     * 保存订单
     *
     * @return
     */
    @Api(name = "saveOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public String saveOrder(IOT4OrderBean iot4OrderBean) {

        Member member = MemberUtils.getMember();

        if (member == null) {
            throw new ApiException("未获取到用户信息");
        }

        if (iot4OrderBean.getWeight() == null || iot4OrderBean.getWeight() < 0.0) {
            throw new ApiException("重量为空");
        }

        //获取设备信息
        CompanyEquipment companyEquipment = companyEquipmentService.getByEquipmentCode(iot4OrderBean.getDeviceCode());

        if (companyEquipment == null) {
            throw new ApiException("设备不存在");
        }

        //查找公司
        Company company = companyService.selectById(companyEquipment.getCompanyId());

        if (company == null) {
            throw new ApiException("服务商不存在");
        }

        //根据服务商查找服务商绑定的品类是否存在

        IotCompanyCategory companyCategory = iotCompanyCategoryService.selectByCategoryCodeAndCompanyId(iot4OrderBean.getCategoryCode(), company.getId());

        return iotService.saveOrder(iot4OrderBean, companyEquipment.getAddress(), company.getName(), company.getId(), companyCategory, member.getAliUserId());

    }


    /**
     * 完成订单
     *
     * @return
     */
    @Api(name = "completeOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Object completeOrder(IOT4OrderBean iot4OrderBean) {
        return iotService.completeOrder(iot4OrderBean.getOrderNo(), iot4OrderBean.getOrderType());

    }


    /**
     * 取消订单
     *
     * @return
     */
    @Api(name = "cancelOrder", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public String cancelOrder(IOT4OrderBean iot4OrderBean) {
        iotService.cancelOrder(iot4OrderBean.getOrderNo());
        return "succcess";
    }



}

package com.skin.api.admin;

import com.skin.core.service.TakeOrderService;
import com.skin.params.TakeOrderBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 14:42
 * @Description:
 */
@ApiService
public class TakeOrderApi {

    @Autowired
    TakeOrderService takeOrderService;

    @Api(name = "order.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOrderPage(TakeOrderBean takeOrderBean) {
        return takeOrderService.getPage(takeOrderBean.getPageBean().getPageNum(), takeOrderBean.getPageBean().getPageSize(),
                takeOrderBean.getEmail(), takeOrderBean.getNickName());
    }

    @Api(name = "order.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getOne(TakeOrderBean takeOrderBean) {
        return takeOrderService.getById(takeOrderBean.getId());
    }

    @Api(name = "order.changeStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object changeStatus(TakeOrderBean takeOrderBean) {
        takeOrderService.changeStatus(takeOrderBean.getId(), takeOrderBean.getStatus());
        return "success";
    }

}

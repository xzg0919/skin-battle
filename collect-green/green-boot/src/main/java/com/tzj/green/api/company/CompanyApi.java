package com.tzj.green.api.company;

import com.tzj.green.service.OrderCountService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

@ApiService
public class CompanyApi {
    @Resource
    private OrderCountService orderCountService;

    /**
     * 杭州城管局获取解衣科技数据信息
     *
     */
    @Api(name = "company.list.get", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getList(){
        return orderCountService.getOrderCount1();
    }
}

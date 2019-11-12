package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.AdminReception;

public interface AdminReceptionService extends IService<AdminReception> {

    @DS("slave")
    Object getAdminReceptionToken(String userName,String password);

}

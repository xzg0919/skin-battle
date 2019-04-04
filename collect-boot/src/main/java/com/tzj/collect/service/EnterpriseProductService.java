package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseProductBean;
import com.tzj.collect.entity.EnterpriseProduct;

public interface EnterpriseProductService extends IService<EnterpriseProduct> {


    /**
     * 更改/新增以旧换新的产品信息
     * 王灿
     * @param enterpriseProductBean
     * @return
     */
    Object updateEnterpriseProduct(EnterpriseProductBean enterpriseProductBean,Integer enterpriseId) throws ApiException;
}

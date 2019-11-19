package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.entity.EnterpriseCode;

import java.util.List;
import java.util.Map;

public interface EnterpriseCodeService extends IService<EnterpriseCode> {

    /**
     * 新增以旧换新的相关券的信息
     * wangcan
     * @param
     * @return
     */
    Object saveEnterpriseCode(long erminalId, EnterpriseCodeBean enterpriseCodeBean) throws Exception;
    /**
     * 以旧换新的券的列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    @DS("slave")
    Object enterpriseCodeList(EnterpriseCodeBean enterpriseCodeBean, Integer enterpriseId)throws Exception;
    /**
     * 以旧换新的券的详情
     * 王灿
     * @param enterpriseCodeId
     * @return
     */
    @DS("slave")
    Object enterpriseCodeDetil(String enterpriseCodeId);
    /**
     * 旧换新的券的excel列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    @DS("slave")
    List<Map<String,Object>> outEnterpriseCodeExcel(EnterpriseCodeBean enterpriseCodeBean, Integer enterpriseId)throws Exception;
}

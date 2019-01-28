package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseCodeBean;
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
    Object saveEnterpriseCode(long erminalId,EnterpriseCodeBean enterpriseCodeBean) throws ApiException;
    /**
     * 以旧换新的券的列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    Object enterpriseCodeList(EnterpriseCodeBean enterpriseCodeBean,Integer enterpriseId)throws ApiException;
    /**
     * 以旧换新的券的详情
     * 王灿
     * @param enterpriseCodeId
     * @return
     */
    Object enterpriseCodeDetil(String enterpriseCodeId);
    /**
     * 旧换新的券的excel列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    List<Map<String,Object>> outEnterpriseCodeExcel(EnterpriseCodeBean enterpriseCodeBean, Integer enterpriseId)throws ApiException;
}

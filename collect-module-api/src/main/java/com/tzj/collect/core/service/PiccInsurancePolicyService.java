package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.core.param.picc.PiccInsurancePolicyBean;
import com.tzj.collect.entity.PiccInsurancePolicy;

public interface PiccInsurancePolicyService extends IService<PiccInsurancePolicy> {

     Object updateInsurance(Long piccCompanyId, PiccInsurancePolicyBean piccInsurancePolicyBean) throws Exception;
     @DS("slave")
     Object getInsurancePolicy(long piccCompanyId);
     @DS("slave")
     Object insuranceDetal(String aliUserId, Integer insuranceId);
}

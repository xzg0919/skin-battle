package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.picc.param.PiccInsurancePolicyBean;
import com.tzj.collect.entity.PiccInsurancePolicy;

public interface PiccInsurancePolicyService extends IService<PiccInsurancePolicy> {

     Object updateInsurance(Long piccCompanyId,PiccInsurancePolicyBean piccInsurancePolicyBean) throws ApiException;
     @DS("slave")
     Object getInsurancePolicy(long piccCompanyId);
     @DS("slave")
     Object insuranceDetal(Integer memberId,Integer insuranceId);
}

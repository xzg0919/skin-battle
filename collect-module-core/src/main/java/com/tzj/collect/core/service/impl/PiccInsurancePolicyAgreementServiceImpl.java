package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.PiccInsurancePolicyAgreementMapper;
import com.tzj.collect.core.service.PiccInsurancePolicyAgreementService;
import com.tzj.collect.entity.PiccInsurancePolicyAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PiccInsurancePolicyAgreementServiceImpl extends ServiceImpl<PiccInsurancePolicyAgreementMapper,PiccInsurancePolicyAgreement> implements PiccInsurancePolicyAgreementService {

    @Autowired
    private PiccInsurancePolicyAgreementMapper piccInsurancePolicyAgreementMapper;
}

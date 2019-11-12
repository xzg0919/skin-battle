package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.EnterpriseAccountMapper;
import com.tzj.collect.core.service.EnterpriseAccountService;
import com.tzj.collect.entity.EnterpriseAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnterpriseAccountServiceImpl extends ServiceImpl<EnterpriseAccountMapper,EnterpriseAccount> implements EnterpriseAccountService {

    @Autowired
    private EnterpriseAccountMapper enterpriseAccountMapper;
}

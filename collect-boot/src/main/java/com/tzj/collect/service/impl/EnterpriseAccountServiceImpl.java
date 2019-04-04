package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.mapper.EnterpriseAccountMapper;
import com.tzj.collect.service.EnterpriseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnterpriseAccountServiceImpl extends ServiceImpl<EnterpriseAccountMapper,EnterpriseAccount> implements EnterpriseAccountService {

    @Autowired
    private EnterpriseAccountMapper enterpriseAccountMapper;
}

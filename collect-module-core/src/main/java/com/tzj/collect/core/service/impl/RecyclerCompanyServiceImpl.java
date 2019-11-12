package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclerCompanyMapper;
import com.tzj.collect.core.service.RecyclerCompanyService;
import com.tzj.collect.entity.RecyclerCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecyclerCompanyServiceImpl extends ServiceImpl<RecyclerCompanyMapper, RecyclerCompany> implements RecyclerCompanyService {

    @Autowired
    private RecyclerCompanyMapper recyclerCompanyMapper;

}

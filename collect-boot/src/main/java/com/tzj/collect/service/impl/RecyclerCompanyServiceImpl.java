package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RecyclerCompany;
import com.tzj.collect.mapper.RecyclerCompanyMapper;
import com.tzj.collect.service.RecyclerCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecyclerCompanyServiceImpl extends ServiceImpl<RecyclerCompanyMapper, RecyclerCompany> implements RecyclerCompanyService {

    @Autowired
    private RecyclerCompanyMapper recyclerCompanyMapper;

}

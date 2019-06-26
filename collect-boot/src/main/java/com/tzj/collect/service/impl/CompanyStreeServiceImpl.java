package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.CompanyStree;
import com.tzj.collect.mapper.CompanyStreeMapper;
import com.tzj.collect.service.CompanyStreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CompanyStreeServiceImpl extends ServiceImpl<CompanyStreeMapper,CompanyStree> implements CompanyStreeService{

    @Autowired
    private CompanyStreeMapper companyStreeMapper;


    @Override
    public Integer selectStreeCompanyIds(Integer categoryId, Integer streetId) {


        return companyStreeMapper.selectStreeCompanyIds(categoryId,streetId);
    }
}

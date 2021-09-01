package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyStreeMapper;
import com.tzj.collect.core.service.CompanyStreeService;
import com.tzj.collect.entity.CompanyStree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class CompanyStreeServiceImpl extends ServiceImpl<CompanyStreeMapper,CompanyStree> implements CompanyStreeService {

    @Autowired
    private CompanyStreeMapper companyStreeMapper;


    @Override
    public Integer selectStreeCompanyIds(Integer categoryId, Integer streetId) {
        return companyStreeMapper.selectStreeCompanyIds(categoryId,streetId);
    }

    @Override
    public Integer selectCount(Long companyId, Long streetId, Long districtId) {
        return selectCount(new EntityWrapper<CompanyStree>().eq("company_id",companyId).eq("stree_id",streetId)
                .eq("area_id",districtId).eq("del_flag",0));
    }

    @Override
    public List<String> findCompanyCityName(Long companyId) {
        return companyStreeMapper.findCompanyCityName(companyId);
    }


}

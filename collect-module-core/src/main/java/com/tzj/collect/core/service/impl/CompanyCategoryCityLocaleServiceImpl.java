package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCategoryCityLocaleMapper;
import com.tzj.collect.core.result.business.CategoryResult;
import com.tzj.collect.core.service.CompanyCategoryCityLocaleService;
import com.tzj.collect.entity.CompanyCategoryCityLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class CompanyCategoryCityLocaleServiceImpl extends ServiceImpl<CompanyCategoryCityLocaleMapper, CompanyCategoryCityLocale> implements CompanyCategoryCityLocaleService {

    @Autowired
    private CompanyCategoryCityLocaleMapper companyCategoryCityLocaleMapper;

    @Override
    public List<CategoryResult> getHouseHoldDetailLocale(String parentId, String companyId, String cityId) {
        return companyCategoryCityLocaleMapper.getHouseHoldDetailLocale(parentId,companyId,cityId);
    }

}

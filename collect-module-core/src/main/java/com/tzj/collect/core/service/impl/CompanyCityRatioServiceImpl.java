package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyCityRatioMapper;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.service.CompanyCityRatioService;
import com.tzj.collect.entity.CompanyCityRatio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;


@Service
public class CompanyCityRatioServiceImpl extends ServiceImpl<CompanyCityRatioMapper, CompanyCityRatio> implements CompanyCityRatioService {


    @Override
    @Transactional
    public String updateCompanyCityRatio(AreaBean areaBean){
        CompanyCityRatio companyCityRatio = this.selectOne(new EntityWrapper<CompanyCityRatio>().eq("company_id", areaBean.getCompanyId()).eq("city_id", areaBean.getCityId()));
        if (null == companyCityRatio){
            companyCityRatio = new CompanyCityRatio();
        }
        companyCityRatio.setCityId(Integer.parseInt(areaBean.getCityId()));
        companyCityRatio.setCompanyId(Integer.parseInt(areaBean.getCompanyId()));
        companyCityRatio.setRatio(new BigDecimal(areaBean.getRatio()));
        this.insertOrUpdate(companyCityRatio);
        return "操作成功";
    }





}

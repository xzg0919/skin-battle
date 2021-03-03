package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import com.tzj.collect.core.mapper.CompanyStreeMapper;
import com.tzj.collect.core.mapper.CompanyStreetAppSmallMapper;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly=true)
public class CompanyStreetAppSmallServiceImpl extends ServiceImpl<CompanyStreetAppSmallMapper, CompanyStreetAppSmall> implements CompanyStreetAppSmallService {

    @Autowired
    private CompanyStreetAppSmallMapper companyStreetAppSmallMapper;
    @Autowired
    private CompanyCategoryCityNameService companyCategoryCityNameService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AreaService areaService;


    @Override
    public Integer selectStreetAppSmallCompanyIds(Integer cityId, Integer streetId) {
       Integer companyId = companyStreetAppSmallMapper.selectStreetAppSmallCompanyIds(streetId);
        if(companyId == null){
            return null;
        }
        Area area = areaService.selectById(cityId);
        /**
         * 安徽省
         * 江苏省
         * 浙江省 低价值回收物中包含小家电回收则 不允许下单首页小家电回收
         */
        Integer[] provinces = new Integer[]{14633,11465,13135};
        if(!Arrays.toString(provinces).contains(area.getParentId().toString())){
            return companyId;
        }
        Integer count = companyCategoryCityNameService.selectCount(new EntityWrapper<CompanyCategoryCityName>().eq("city_id", cityId).eq("parent_id", 70).eq("company_id", companyId).eq("del_flag", 0));
        if( count > 0){
            return null;
        }else {
            return companyId;
        }
    }

    public static void main(String[] args) {
        Integer[] provinces = new Integer[]{14633,11465,13135};
        System.out.println(Arrays.toString(provinces).contains("11465"));
    }
}

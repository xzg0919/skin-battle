package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.IotCategoryMapper;
import com.tzj.collect.core.mapper.IotCompanyCategoryMapper;
import com.tzj.collect.core.service.IotCategoryService;
import com.tzj.collect.core.service.IotCompanyCategoryService;
import com.tzj.collect.entity.IotCategory;
import com.tzj.collect.entity.IotCompanyCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IotCompanyCategoryServiceImpl extends ServiceImpl<IotCompanyCategoryMapper, IotCompanyCategory> implements IotCompanyCategoryService {

    @Autowired
    private IotCompanyCategoryMapper  iotCompanyCategoryMapper;

    @Override
    public IotCompanyCategory selectByCategoryCodeAndCompanyId(String categoryCode, Long companyId) {
        return this.selectOne(new EntityWrapper<IotCompanyCategory>().eq("category_code",categoryCode)
                .eq("del_flag","0").eq("company_id",companyId));
    }
}

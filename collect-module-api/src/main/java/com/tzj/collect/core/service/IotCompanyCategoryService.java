package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.IotCategory;
import com.tzj.collect.entity.IotCompanyCategory;


public interface IotCompanyCategoryService extends IService<IotCompanyCategory>{

  IotCompanyCategory selectByCategoryCodeAndCompanyId(String categoryCode,Long companyId);

}

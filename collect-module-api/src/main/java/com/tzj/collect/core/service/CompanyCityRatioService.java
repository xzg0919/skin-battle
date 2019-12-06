package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.entity.CompanyCityRatio;

import java.util.Map;

public interface CompanyCityRatioService extends IService<CompanyCityRatio> {

    String updateCompanyCityRatio(AreaBean areaBean);

}

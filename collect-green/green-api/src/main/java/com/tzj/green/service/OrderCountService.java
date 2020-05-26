package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.param.CompanyBean;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface OrderCountService extends IService<T> {

    List<Map<String, Object>> getOrderCount();

    List<Map<String, Object>> getPointCount(CompanyBean companyBean);

     Object getOrderCount1();
}

package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface OrderCountService extends IService<T> {

    List<Map<String, Object>> getOrderCount();

     Object getOrderCount1();
}

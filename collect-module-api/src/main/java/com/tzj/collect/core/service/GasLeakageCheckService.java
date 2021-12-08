package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.GasLeakageCheck;

import java.util.List;


public interface GasLeakageCheckService extends IService<GasLeakageCheck>{


    List<GasLeakageCheck> getListByCreateDate(String beginDate,String endDate);
}

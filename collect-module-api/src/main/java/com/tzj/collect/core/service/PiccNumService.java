package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.PiccNum;

import java.util.Map;

public interface PiccNumService extends IService<PiccNum> {
    @DS("slave")
    Map<String,Object> selectPiccNum(long piccConpanyId);
    @DS("slave")
    Map<String, Object> selectPiccErrorNum(long piccConpanyId);
    @DS("slave")
    Map<String, Object> selectPiccSuccessNum(long piccConpanyId);
}

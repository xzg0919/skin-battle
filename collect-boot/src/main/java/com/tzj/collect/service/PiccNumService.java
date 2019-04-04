package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.PiccNum;

import java.util.Map;

public interface PiccNumService extends IService<PiccNum> {

    Map<String,Object> selectPiccNum(long piccConpanyId);

    Map<String, Object> selectPiccErrorNum(long piccConpanyId);

    Map<String, Object> selectPiccSuccessNum(long piccConpanyId);
}

package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.SN;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public interface SNService extends IService<SN> {

    public Integer updateTest(SN sn);
}

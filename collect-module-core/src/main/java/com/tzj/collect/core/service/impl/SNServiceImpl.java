package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.SNMapper;
import com.tzj.collect.core.service.SNService;
import com.tzj.collect.entity.SN;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service
@Transactional(readOnly = true)
public class SNServiceImpl extends ServiceImpl<SNMapper, SN> implements SNService {

    @Resource
    private SNMapper snMapper;

    @Transactional(readOnly = false)
    public Integer updateTest(SN sn) {
        return snMapper.updateById(sn);
    }
}

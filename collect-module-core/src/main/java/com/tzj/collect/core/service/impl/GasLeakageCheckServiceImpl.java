package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.GasLeakageCheckMapper;
import com.tzj.collect.core.service.GasLeakageCheckService;
import com.tzj.collect.entity.GasLeakageCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiangzhongguo
 */
@Service
@Transactional(readOnly = true)
public class GasLeakageCheckServiceImpl extends ServiceImpl<GasLeakageCheckMapper, GasLeakageCheck> implements GasLeakageCheckService {

    @Autowired
    private GasLeakageCheckMapper mapper;

}

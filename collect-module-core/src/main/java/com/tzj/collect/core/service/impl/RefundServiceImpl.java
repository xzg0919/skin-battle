package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RefundMapper;
import com.tzj.collect.core.service.RefundService;
import com.tzj.collect.entity.Refund;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

}

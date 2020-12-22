package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.service.FlcxMerchantService;
import com.tzj.collect.core.service.FlcxMerchantThresHoldService;
import com.tzj.collect.entity.FlcxMerchant;
import com.tzj.collect.entity.FlcxMerchantThresHold;
import com.tzj.collect.mapper.FlcxMerchantMapper;
import com.tzj.collect.mapper.FlcxMerchantThresHoldMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**、
 * 分类指南 商户 阈值
 */
@Component
@Service(version = "${flcx.service.version}")
@Transactional(readOnly = true)
public class FlcxMerchantThresHoldServiceImpl extends ServiceImpl<FlcxMerchantThresHoldMapper, FlcxMerchantThresHold> implements FlcxMerchantThresHoldService {



}

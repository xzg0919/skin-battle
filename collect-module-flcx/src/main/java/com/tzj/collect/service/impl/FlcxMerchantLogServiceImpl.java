package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.param.flcx.FlcxEggshellBean;
import com.tzj.collect.core.service.FlcxEggshellService;
import com.tzj.collect.core.service.FlcxMerchantLogService;
import com.tzj.collect.entity.FlcxEggshell;
import com.tzj.collect.entity.FlcxMerchantLog;
import com.tzj.collect.mapper.FlcxEggshellMapper;
import com.tzj.collect.mapper.FlcxMerchantLogMapper;
import com.tzj.collect.mapper.FlcxMerchantMapper;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类指南 商户调用日志
 */
@Component
@Service(version = "${flcx.service.version}")
@Transactional(readOnly = true)
public class FlcxMerchantLogServiceImpl extends ServiceImpl<FlcxMerchantLogMapper, FlcxMerchantLog> implements FlcxMerchantLogService {



}

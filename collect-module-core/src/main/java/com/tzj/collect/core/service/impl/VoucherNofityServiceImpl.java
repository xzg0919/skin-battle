package com.tzj.collect.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.VoucherNofityMapper;
import com.tzj.collect.core.service.VoucherNofityService;
import com.tzj.collect.entity.VoucherNofity;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [优惠券通知service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VoucherNofityServiceImpl extends ServiceImpl<VoucherNofityMapper, VoucherNofity> implements VoucherNofityService
{
    @Resource
    private VoucherNofityMapper VoucherNofityMapper;

}
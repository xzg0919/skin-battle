package com.tzj.collect.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.ProductCodeMapper;
import com.tzj.collect.core.service.ProductCodeService;
import com.tzj.collect.entity.ProductCode;

/**
 * 
 * <p>Created on 2019年12月3日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [商品券码ServiceImpl]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true)
public class ProductCodeServiceImpl extends ServiceImpl<ProductCodeMapper, ProductCode> implements ProductCodeService
{

}

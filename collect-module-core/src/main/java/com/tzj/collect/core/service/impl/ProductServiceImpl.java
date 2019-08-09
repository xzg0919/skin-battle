package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.ProductMapper;
import com.tzj.collect.core.service.ProductService;
import com.tzj.collect.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService {

}

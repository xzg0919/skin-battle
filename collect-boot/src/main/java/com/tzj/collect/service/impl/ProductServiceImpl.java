package com.tzj.collect.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Product;
import com.tzj.collect.mapper.ProductMapper;
import com.tzj.collect.service.ProductService;

@Service
@Transactional(readOnly=true)
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService{

}

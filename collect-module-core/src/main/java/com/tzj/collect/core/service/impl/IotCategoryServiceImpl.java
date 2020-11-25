package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.AdminMapper;
import com.tzj.collect.core.mapper.IotCategoryMapper;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.core.service.IotCategoryService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.IotCategory;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@Service
@Transactional(readOnly = true)
public class IotCategoryServiceImpl extends ServiceImpl<IotCategoryMapper, IotCategory> implements IotCategoryService {

    @Autowired
    private IotCategoryMapper iotCategoryMapper;

}

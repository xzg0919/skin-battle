package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.BlackListMapper;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 黑名单Impl
 *
 *
 */
@Service
@Transactional(readOnly = true)
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {



}

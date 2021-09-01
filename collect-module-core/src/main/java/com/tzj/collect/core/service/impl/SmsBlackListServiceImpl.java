package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.SmsBlackListMapper;
import com.tzj.collect.core.service.SmsBlackListService;
import com.tzj.collect.entity.SmsBlackList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 黑名单Impl
 */
@Service
@Transactional(readOnly = true)
public class SmsBlackListServiceImpl extends ServiceImpl<SmsBlackListMapper, SmsBlackList> implements SmsBlackListService {

    @Override
    public SmsBlackList findByTel(String tel) {
        return selectOne(new EntityWrapper<SmsBlackList>().eq("tel", tel).eq("del_flag", "0"));
    }

}

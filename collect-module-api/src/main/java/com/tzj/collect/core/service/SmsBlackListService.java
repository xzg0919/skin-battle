package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.SmsBlackList;

/**
 * @Author 王灿
 **/
public interface SmsBlackListService extends IService<SmsBlackList> {
    SmsBlackList findByTel(String tel);
}

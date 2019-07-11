package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.ImprisonRule;

public interface ImprisonRuleService extends IService<ImprisonRule> {

    //判断用户是否满足加入黑名单要求
    Boolean isImprisonRuleByAliUserId(String aliUserId,String title);

}

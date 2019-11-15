package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.ImprisonMember;

public interface ImprisonMemberService extends IService<ImprisonMember> {

    //判断用户是否在黑名单中
    @DS("slave")
    Boolean isImprisonMember(String aliUserId, String title);
}

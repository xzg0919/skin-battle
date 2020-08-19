package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.MemberAliAccount;

public interface MemberAliAccountService extends IService<MemberAliAccount> {


    Object saveAliAccount(String aliAccount, String aliUserId);

    Object deleteAliAccountById(String id, String aliUserId);

    Object getAliAccountList(String aliUserId);
}


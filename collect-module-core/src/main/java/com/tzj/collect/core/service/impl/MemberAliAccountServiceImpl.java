package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.MemberAliAccountMapper;
import com.tzj.collect.core.service.MemberAliAccountService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAliAccount;
import com.tzj.collect.entity.MemberXianyu;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAliAccountServiceImpl extends ServiceImpl<MemberAliAccountMapper, MemberAliAccount> implements MemberAliAccountService {


    @Override
    @Transactional
    public Object saveAliAccount(String aliAccount, String aliUserId) {
        //查询用户在支付宝是否存在
        MemberAliAccount memberAliAccount = this.selectOne(new EntityWrapper<MemberAliAccount>().eq("ali_user_id", aliUserId).eq("ali_account", aliAccount));
        if (null == memberAliAccount){
            memberAliAccount = new MemberAliAccount();
            memberAliAccount.setAliAccount(aliAccount);
            memberAliAccount.setAliUserId(aliUserId);
            this.insert(memberAliAccount);
        }
        return "操作成功";
    }

    @Override
    @Transactional
    public Object deleteAliAccountById(String id, String aliUserId) {
        this.deleteById(Long.parseLong(id));
        return "操作成功";
    }

    @Override
    public Object getAliAccountList(String aliUserId) {
        return selectList(new EntityWrapper<MemberAliAccount>().eq("ali_user_id",aliUserId));
    }
}

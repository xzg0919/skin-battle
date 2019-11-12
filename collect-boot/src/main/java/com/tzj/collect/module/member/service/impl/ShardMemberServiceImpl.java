package com.tzj.collect.module.member.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Member;
import com.tzj.collect.module.member.mapper.ShardMemberMapper;
import com.tzj.collect.module.member.service.ShardMemberService;

public class ShardMemberServiceImpl extends ServiceImpl<ShardMemberMapper, Member>  implements ShardMemberService {
    @Override
    public String getTableNameByAliId(String aliId) {
        return null;
    }

    @Override
    public boolean updateMemberByAliId() {
        return false;
    }
}

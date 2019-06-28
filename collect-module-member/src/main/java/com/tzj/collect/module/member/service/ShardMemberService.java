package com.tzj.collect.module.member.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.*;
/**
 *  用户分表Service
 */
public interface ShardMemberService extends IService<Member> {
    String getTableNameByAliId(String aliId);
    boolean updateMemberByAliId();

}

package com.tzj.collect.module.member.service;

/**
 *  用户分表Service
 */
public interface ShardMemberService {
    String getTableNameByAliId(String aliId);
    boolean updateMemberByAliId();

}

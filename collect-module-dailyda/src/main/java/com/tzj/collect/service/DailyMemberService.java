package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Member;

import java.util.Map;

/**
 * @author sgmark
 * @create 2019-08-29 9:36
 **/
public interface DailyMemberService extends IService<Member> {
    @DS("slave")
    Map<String, Object> selectMemberInfoByAliUserId(String aliUserId);
}

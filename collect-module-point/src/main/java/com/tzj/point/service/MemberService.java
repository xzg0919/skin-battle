package com.tzj.point.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Member;

import java.util.Map;

/**
 * @Author 王灿
 **/
public interface MemberService extends IService<Member>{

    /**
     * 根据阿里UserId查询唯一数据
     * @param aliUserId
     * @return
     */
    @DS("slave")
    Member selectMemberByAliUserId(String aliUserId);

    /**
     * 根据阿里UserId更新相关数据
     * @param member
     * @return
     */
    Integer updateMemberByAliUserId(Member member);

    /**
     * 根据新增一条会员数据
     * @param member
     * @return
     */
    Integer insertMember(Member member);
    /**
     * 更新或插入会员数据
     * @param member
     * @return
     */
    Integer inserOrUpdatetMember(Member member);


}

package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Member;
import org.apache.ibatis.annotations.Param;

/**
 * @Author 王灿
 **/
public interface MemberMapper extends BaseMapper<Member>{
    /**
     * 根据阿里UserId查询唯一数据
     * @param aliUserId
     * @return
     */
    Member selectMemberByAliUserId(@Param("aliUserId") String aliUserId,@Param("tableName") String tableName);

    /**
     * 根据阿里UserId更新相关数据
     * @param member
     * @return
     */
    Integer updateMemberByAliUserId(@Param("member")Member member);

    /**
     * 根据新增一条会员数据
     * @param member
     * @return
     */
    Integer insertMember(@Param("member")Member member);
}

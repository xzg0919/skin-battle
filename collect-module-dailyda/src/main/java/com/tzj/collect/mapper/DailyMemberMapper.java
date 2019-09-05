package com.tzj.collect.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/8/15 0015
  * @param
  * @return
  */
public interface DailyMemberMapper extends BaseMapper<Member> {

    Map<String, Object> selectMemberInfoByAliUserId(@Param("aliUserId") String aliUserId, @Param("tableName") String tableName);

    /**
     * 根据阿里UserId查询唯一数据
     * @param aliUserId
     * @return
     */
    Member selectMemberByAliUserId(@Param("aliUserId") String aliUserId,@Param("tableName") String tableName);
    /** 查询所有formId不为空的用户
      * @author sgmark@aliyun.com
      * @date 2019/9/4 0004
      * @param
      * @return
      */
    @DS("slave_1")
    List<Map<String, Object>> allExitsFormIdMemberList();
}

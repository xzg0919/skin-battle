package com.tzj.collect.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
  * @author sgmark@aliyun.com
  * @date 2019/8/15 0015
  * @param
  * @return
  */
public interface DailyMemberMapper extends BaseMapper<Member> {

    Map<String, Object> selectMemberInfoByAliUserId(@Param("aliUserId") String aliUserId, @Param("tableName") String tableName);

}

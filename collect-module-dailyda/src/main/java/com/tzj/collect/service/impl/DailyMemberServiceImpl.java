package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Member;
import com.tzj.collect.mapper.DailyMemberMapper;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.collect.service.DailyMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 答答答memberService
 *
 * @author sgmark
 * @create 2019-08-29 9:35
 **/
@Service
@Transactional(readOnly = true)
public class DailyMemberServiceImpl extends ServiceImpl<DailyMemberMapper, Member> implements DailyMemberService {

    @Resource
    private DailyMemberMapper dailyMemberMapper;

    @Override
    public Map<String, Object> selectMemberInfoByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", Long.parseLong(aliUserId), 40);
        return dailyMemberMapper.selectMemberInfoByAliUserId(aliUserId,memberName);
    }
}

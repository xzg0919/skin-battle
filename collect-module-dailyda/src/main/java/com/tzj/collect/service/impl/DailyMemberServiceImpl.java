package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Member;
import com.tzj.collect.mapper.DailyMemberMapper;
import com.tzj.collect.common.shard.ShardTableHelper;
import com.tzj.collect.core.service.DailyMemberService;
import com.tzj.collect.core.service.DailySendMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 答答答memberService
 *
 * @author sgmark
 * @create 2019-08-29 9:35
 **/
@Service
@DS("slave")
@Transactional(readOnly = true)
public class DailyMemberServiceImpl extends ServiceImpl<DailyMemberMapper, Member> implements DailyMemberService {

    @Resource
    private DailyMemberMapper dailyMemberMapper;

    @Resource
    private DailySendMessageService dailySendMessageService;

    @Override
    public Map<String, Object> selectMemberInfoByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", aliUserId, 40);
        return dailyMemberMapper.selectMemberInfoByAliUserId(aliUserId,memberName);
    }

    @Override
    public Member selectMemberByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", aliUserId, 40);
        return dailyMemberMapper.selectMemberByAliUserId(aliUserId,memberName);
    }

    @Override
    public void sendMsgToAllMemberThread() {
        //查询所有用户阿里usrId及formId
        List<Map<String, Object>> userList = dailyMemberMapper.allExitsFormIdMemberList();
        //发送模板消息
        if (userList.size() <= 10000){
            userList.stream().forEach(userLists -> {
                dailySendMessageService.sendMsgToAllMember(userLists.get("ali_user_id") + "", userLists.get("form_id") + "");
            });
        }else {
            userList.parallelStream().forEach(userLists -> {
                dailySendMessageService.sendMsgToAllMember(userLists.get("ali_user_id") + "", userLists.get("form_id") + "");
            });
        }
    }
}

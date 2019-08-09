package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.mapper.ImprisonRuleMapper;
import com.tzj.collect.core.service.ImprisonMemberService;
import com.tzj.collect.core.service.ImprisonRuleService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.ImprisonMember;
import com.tzj.collect.entity.ImprisonRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional(readOnly = true)
public class ImprisonRuleServiceImpl extends ServiceImpl<ImprisonRuleMapper, ImprisonRule> implements ImprisonRuleService {
    @Autowired
    private ImprisonRuleMapper imprisonRuleMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ImprisonMemberService imprisonMemberService;

    //判断用户是否满足列入黑名单需求
    @Transactional
    public Boolean isImprisonRuleByAliUserId(String aliUserId,String title){
        Boolean isImprisonRule = false;
        ImprisonRule imprisonRule = this.selectOne(new EntityWrapper<ImprisonRule>().eq("is_enable", 0).eq("title",title));
        if(null == imprisonRule){
            return isImprisonRule;
        }
        isImprisonRule = orderService.selectOrderByImprisonRule(aliUserId,imprisonRule.getTitle(),imprisonRule.getOrderNum(),imprisonRule.getDateNum());
        if(isImprisonRule){
            ImprisonMember imprisonMember = imprisonMemberService.selectOne(new EntityWrapper<ImprisonMember>().eq("ali_user_id", aliUserId).eq("title",title));
            Date startDate = new Date();
            Date endDate = ToolUtils.addDateByNow(startDate,imprisonRule.getImprisonDateNum()-1);
            startDate = ToolUtils.getDateTime(ToolUtils.getDateToString(startDate)+" 00:00:01");
            endDate = ToolUtils.getDateTime(ToolUtils.getDateToString(endDate)+" 23:59:59");
            if(null == imprisonMember){
                imprisonMember = new ImprisonMember();
            }else {
                if(imprisonMember.getEndDate().getTime()>=endDate.getTime()){
                    return isImprisonRule;
                }
            }
            imprisonMember.setAliUserId(aliUserId);
            imprisonMember.setStartDate(startDate);
            imprisonMember.setEndDate(endDate);
            imprisonMember.setTitle(title);
            imprisonMemberService.insertOrUpdate(imprisonMember);
            System.out.println("禁用开始时间："+ToolUtils.getDateTimeToString(startDate)+"-------禁用结束时间："+ToolUtils.getDateTimeToString(endDate)+"-----禁用类型："+imprisonRule.getTitle());
        }
        return isImprisonRule;
    }

}

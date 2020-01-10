package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import javax.swing.*;

import com.tzj.green.entity.Member;
import com.tzj.green.entity.MemberPoints;
import com.tzj.green.mapper.MemberMapper;
import com.tzj.green.mapper.MemberPointsMapper;
import com.tzj.green.param.MemberBean;
import com.tzj.green.param.PageBean;
import com.tzj.green.service.MemberPointsService;
import com.tzj.green.service.MemberService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户会员表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService
{
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberPointsService memberPointsService;


    @Override
    public Object getMemberList(MemberBean memberBean, Long companyId) {
        PageBean pageBean = memberBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        List<Map<String, Object>> memberList = memberMapper.getMemberList(companyId, memberBean.getStartTime(), memberBean.getEndTime(), memberBean.getProvinceId(), memberBean.getCityId(), memberBean.getAreaId(), memberBean.getStreetId(), memberBean.getCommunityId(), memberBean.getCommunityHouseId(), memberBean.getName(), memberBean.getTel(), pageStart, pageBean.getPageSize());
        Integer count = memberMapper.getMemberCount(companyId, memberBean.getStartTime(), memberBean.getEndTime(), memberBean.getProvinceId(), memberBean.getCityId(), memberBean.getAreaId(), memberBean.getStreetId(), memberBean.getCommunityId(), memberBean.getCommunityHouseId(), memberBean.getName(), memberBean.getTel());
        Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("memberList",memberList);
            resultMap.put("count",count);
            resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }

    @Override
    @Transactional
    public Object updateIsCancelByRealNo(String realNo) {
        Member member = this.selectOne(new EntityWrapper<Member>().eq("real_no", realNo));
        member.setIsCancel("1");
        this.updateById(member);
        return "操作成功";
    }

    @Override
    public Object getMemberByAliUserId(String aliUserId) {
        Member member = this.selectOne(new EntityWrapper<Member>().eq("ali_user_id", aliUserId));

        Map<String, Object> result = new HashMap<>(4);
        if(member == null){
            throw new ApiException("未登录注册");
        }
        result.put("tatalPoints", 0);
        result.put("userName", member.getName());
        result.put("avatarUrl", member.getAvatarUrl());
        result.put("address", member.getAddress() == null ? "" : member.getAddress() + member.getDetailAddress());
        MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("user_no", member.getRealNo()));
        if(memberPoints != null){
            Long tatalPoints = memberPoints.getTatalPoints();
            result.put("tatalPoints", tatalPoints);
        }
        return result;
    }


    @Override
    public Object getRealNoByAliUserId(String aliUserId) {
        Member me = this.selectOne(new EntityWrapper<Member>().eq("ali_user_id", aliUserId));
        Map<String, Object> result = new HashMap<>(2);
        //五实体卡 status=0
        result.put("status", 0);
        if(null == me || StringUtils.isBlank(me.getRealNo())){
            return result;
        }
        //有实体卡 status=1
        result.put("status", 1);
        String realNo = me.getRealNo();
        result.put("realNo", realNo);
        return result;
    }

}
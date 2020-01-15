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
import com.tzj.green.service.MessageService;
import com.tzj.green.utils.HttpUtil;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Configuration
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService
{
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberPointsService memberPointsService;
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private MessageService messageService;
    @Value("${yidairen.url}")
    private String url;


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



    /**
     * 查找该用户实体卡
     * @param aliUserId
     * @return
     */
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

    /**
     * 扫描绑定实体卡
     * @param memberBean
     * @return
     */
    @Override
    @Transactional
    public Object scanRealCode(MemberBean memberBean) {
        String realNo = memberBean.getRealNo();
        if(StringUtils.isBlank(realNo)){
            throw new  ApiException("扫描实体卡失败");
        }
        if(StringUtils.isBlank(memberBean.getAliUserId())){
            throw new  ApiException("未传入aliUserId");
        }
        Member member = this.selectOne(new EntityWrapper<Member>().eq("real_no", realNo).eq("del_flag", "0"));
        if(member != null && "0".equals(member.getIsCancel())){
            if(StringUtils.isNotBlank(member.getAliUserId())){
                throw new ApiException("实体卡已被绑定");
            }
            throw new  ApiException("卡号不存在或已注销");
        }

        if(messageService.validMessage(memberBean.getTel(), memberBean.getSecurityCode())){
            if(member==null){
                member = new Member();
            }
            member.setAliUserId(memberBean.getAliUserId());
            member.setIsCancel("0");
            member.setCreateDate(new Date());
            member.setMobile(memberBean.getTel());
            MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("real_no", realNo).eq("del_flag", "0"));
            if(memberPoints == null){
                memberPoints = new MemberPoints();
                memberPoints.setRemnantPoints(0L);
                memberPoints.setTatalPoints(0L);
            }
            memberPoints.setAliUserId(member.getAliUserId());
            memberPoints.setUserNo(memberBean.getRealNo());
            memberPointsService.insertOrUpdate(memberPoints);
            this.insertOrUpdate(member);
            return "绑定成功";
        }
        throw new ApiException("验证码错误");
    }

    @Override
    public Object getAllPoints(MemberBean memberBean) {
        String aliUserId = memberBean.getAliUserId();
        Map<String, Object> result = new HashMap<>(2);
        result.put("tatalPoints", 0.0);
        result.put("validPoints", 0.0);
        MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("ali_user_id", aliUserId).eq("del_flag", "0"));
        if(memberPoints != null) {
            result.put("tatalPoints", memberPoints.getTatalPoints());
            result.put("validPoints", memberPoints.getRemnantPoints());
        }
        return result;
    }

}
package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.entity.Member;

import java.util.Map;

/**
 * @Author 王灿
 **/
public interface MemberService extends IService<Member>{
    @DS("slave")
    Member findMemberByAliId(String aliMemberId);

    Member saveByMemberBean(MemberBean memberBean);
    /**
     * 根据用户的code解析用户数据并保存
     * @author 王灿
     * @param authCode
     * @param state 
     * @param cityName
     * @param source
     * @return
     */
    Object getAuthCode(String authCode, String state, String cityName, String source);
    /**
     * 小程序静默授权
     * 根据用户的code解析用户数据并保存
     * @author 王灿
     * @param authCode
     * @param cityName
     * @return
     */
    Object getStaticUserToken(String authCode, String cityName);
    /**
     * 根据用户授权返回的authCode,获取用户的token
     * @author 王灿
     * @param authCode
     * @return
     */
    @DS("slave")
    Object getUserToken(String authCode, String cityName);
    /**
     * 获取会员个人中心的相关数据
     * @author 王灿
     * @param
     */
    @DS("slave")
    Object memberAdmin(String aliUserId);
    @DS("slave")
    Map memberIsExist(MemberBean memberBean);
    @DS("slave")
    Object getPassIdUrl(String aliUserId);
    @DS("slave")
    Object userToken(String authCode);
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

    String saveChannelId(String aliUserId,String channelId);

    /**
     * 获取会员总数
     * @return
     */
    long getMemberCount();
    /**
     * 获取当天会员总数
     * @return
     */
    long getMemberCountToDay();
}

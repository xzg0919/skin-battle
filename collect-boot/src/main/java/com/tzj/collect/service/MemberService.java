package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.entity.Member;

import java.util.Map;

/**
 * @Author 王灿
 **/
public interface MemberService extends IService<Member>{
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
    Object getAuthCode(String authCode,String state,String cityName,String source);
    /**
     * 小程序静默授权
     * 根据用户的code解析用户数据并保存
     * @author 王灿
     * @param authCode
     * @param cityName
     * @return
     */
    Object getStaticUserToken(String authCode,String cityName);
    /**
     * 根据用户授权返回的authCode,获取用户的token
     * @author 王灿
     * @param authCode
     * @return
     */
    Object getUserToken(String authCode,String cityName);
    /**
     * 获取会员个人中心的相关数据
     * @author 王灿
     * @param
     */
    Object memberAdmin(Integer memberId);

    Map memberIsExist(MemberBean memberBean);
}

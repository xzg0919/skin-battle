package com.tzj.point.service.impl;

import com.alipay.api.response.AlipayMarketingCardQueryResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.entity.*;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.point.mapper.MemberMapper;
import com.tzj.point.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 会员ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Override
	public Member selectMemberByAliUserId(String aliUserId) {
		String memberName = ShardTableHelper.getTableNameByModeling("sb_member", Long.parseLong(aliUserId), 40);
		return memberMapper.selectMemberByAliUserId(aliUserId,memberName);
	}

	@Override
	@Transactional
	public Integer updateMemberByAliUserId(Member member) {
		String memberTableName = ShardTableHelper.getTableNameByModeling("sb_member",Long.parseLong(member.getAliUserId()),40);
		member.setTableName(memberTableName);
		return memberMapper.updateMemberByAliUserId(member);
	}

	@Override
	@Transactional
	public Integer insertMember(Member member) {
		String memberTableName = ShardTableHelper.getTableNameByModeling("sb_member",Long.parseLong(member.getAliUserId()),40);
		member.setTableName(memberTableName);
		return memberMapper.insertMember(member);
	}

	@Override
	@Transactional
	public Integer inserOrUpdatetMember(Member member) {
		if(StringUtils.isBlank(member.getAliUserId())){
			return this.insertMember(member);
		}else {
			return this.updateMemberByAliUserId(member);
		}
	}

}

package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.*;
import com.tzj.collect.common.shard.ShardTableHelper;
import com.tzj.point.mapper.MemberMapper;
import com.tzj.collect.core.service.Member4JfAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 会员ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class Member4JfApp4JfAppServiceImpl extends ServiceImpl<MemberMapper, Member> implements Member4JfAppService {

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

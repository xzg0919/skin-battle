package com.tzj.green.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.Member;
import com.tzj.green.param.MemberBean;

import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户会员表service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
public interface MemberService extends IService<Member>
{

    Object getMemberList(MemberBean memberBean, Long companyId);

    Object updateIsCancelByRealNo(String realNo);

    Object getMemberByAliUserId(String aliUserId);

    Object getRealNoByAliUserId(String aliUserId);

    Map<String, Object> memberInfo(MemberBean memberBean);
}
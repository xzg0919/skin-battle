package com.tzj.collect.api.app;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员用户相关Api
 * @author wangcan
 *
 */
@ApiService
public class AppMemberApi {
	@Autowired
	private MemberService memberService;
	@Autowired
	private com.tzj.collect.core.service.MessageService MessageService;
	
	/**
     * 根据用户会员卡的卡号查询用户的信息
     * @author 王灿
     * @param 
     */
    @Api(name = "app.member.getMemberByCard", version = "1.0")
    @SignIgnore 
    @AuthIgnore
	@DS("slave")
    public Object getMemberByCard(MemberBean memberBean) {
    	Map<String,Object> resultMap = new HashMap<String,Object>(); 
    	EntityWrapper wrapper = new EntityWrapper<Member>();
    	wrapper.eq("card_no", memberBean.getCardNo());
    	Member member = memberService.selectOne(wrapper);
    	if(member==null) {
    		resultMap.put("code", "500");
    		resultMap.put("member", "该卡号不存在");
    		return resultMap;
    	}
    	resultMap.put("code", "200");
    	resultMap.put("member",member);
		return resultMap;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

package com.tzj.collect.api.ali;

import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 会员用户相关Api
 * @author wangcan
 *
 */
@ApiService
public class MemberApi {
	@Autowired
	private MemberService memberService;
	@Autowired
	private com.tzj.collect.core.service.MessageService MessageService;

	/**
     * 根据用户授权返回的authCode,解析用户的数据
     * @author 王灿
     * @param 
     */
    @Api(name = "member.getAuthCode", version = "1.0")
    @SignIgnore
    @AuthIgnore 
    public Object getAuthCode(MemberBean memberBean) {
       return memberService.getAuthCode(memberBean.getAuthCode(),memberBean.getState(),memberBean.getCityName(),memberBean.getSource());
    }
    /**
     * 根据用户授权返回的authCode,获取用户的token
     * @author 王灿
     * @param 
     */
    @Api(name = "member.getUserToken", version = "1.0")
    @SignIgnore
    @AuthIgnore 
    public Object getUserToken(MemberBean memberBean) {
       return memberService.getUserToken(memberBean.getAuthCode(),memberBean.getCityName());
    }
	/**
	 * 小程序静默授权
	 * 根据用户授权返回的authCode,获取用户的token
	 * @author 王灿
	 * @param
	 */
	@Api(name = "member.getStaticUserToken", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getStaticUserToken(MemberBean memberBean) {
		return memberService.getStaticUserToken(memberBean.getAuthCode(),memberBean.getCityName());
	}
	/**
	 * 小程序静默授权
	 * 根据用户授权返回的authCode,获取用户的token
	 * @author 王灿
	 * @param
	 */
	@Api(name = "member.ali_user_id", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object userToken(MemberBean memberBean) {
		return memberService.userToken(memberBean.getAuthCode());
	}
    
	/**
     * 根据用提交的短信验证码进行验证
     * @author 王灿
     * @param 
     */
    @Api(name = "member.getMessageCode", version = "1.0")
    @SignIgnore
    @AuthIgnore 
    public Object getMessageCode(MemberBean memberBean) {
    	//用户提交短信验证时 ，进行校验
    	boolean flag = MessageService.validMessage(memberBean.getMobile(),memberBean.getMessageCode());
    	if(!flag) {
    		return "输入的短信验证码有误";
    	}
    	Member member = memberService.selectById(memberBean.getId());
    	if(member!=null) {
    		member.setMobile(memberBean.getMobile());
    	}
    	memberService.updateById(member);
    	Map<String,Object> map = new HashMap<String,Object>();
    	String token= JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
    	String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
    	System.out.println("token:"+securityToken);
		map.put("token", securityToken);
		return map;
    }
    
 

	/**
	 * 返回用户信息
	 * @author
	 * @param
	 */
	@Api(name = "member.getMemberDeatil", version = "1.0")
	@SignIgnore
	public Object getMemberDeatil() {
		//获取当前登录的会员
		Member member = MemberUtils.getMember();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("mobile", StringUtils.isBlank(member.getMobile())?"":member.getMobile());
		resultMap.put("name", StringUtils.isBlank(member.getName())?"":member.getName());
		return resultMap;
	}


	@Api(name = "member.getPassIdUrl", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getPassIdUrl() {
		Member member = MemberUtils.getMember();
		return memberService.getPassIdUrl(member.getAliUserId());
	}
	//保存用户的来源
	/**
	 * 获取会员个人中心的相关数据
	 * @author 王灿
	 * @param
	 */
	@Api(name = "member.saveChannelId", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object saveChannel(MemberBean memberBean) {
		Member member = MemberUtils.getMember();
		return memberService.saveChannelId(member.getAliUserId(),memberBean.getChannelId());
	}

	@Api(name = "member.form.id", version = "1.0")
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Map<String, Object> updateUserFormId(OrderBean orderBean){
		Member member = MemberUtils.getMember();
		if (!StringUtils.isEmpty(orderBean.getFormId())){
			member.setFormId(orderBean.getFormId());
		}
		if (!StringUtils.isEmpty(orderBean.getTemplateId())){
			member.setTemplateId(orderBean.getTemplateId());
		}
		return memberService.updateUserFormId(member);
	}

	/**
	 * 获取会员信息
	 */
	@Api(name = "member.getMember2Point", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getMember(MemberBean memberBean) {
		return memberService.selectMemberByAliUserId(memberBean.getAliUserId());

	}
}

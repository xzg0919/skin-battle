package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.MessageService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
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
	private MessageService MessageService;
	
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
    	String token= JwtUtils.generateToken(member.getId().toString(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
    	String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
    	System.out.println("token:"+securityToken);
		map.put("token", securityToken);
		return map;
    }
    
 
//    /**
//     * 修改弹框状态
//     * @param memberBean
//     * @return
//     */
//    public String updateShowDialog(MemberBean memberBean) {
//		return memberService.updateShowDialog(memberBean);
//	}  
    /**
     * 修改弹框状态
     * @author 
     * @param 
     */
    @Api(name = "member.updateShowDialog", version = "1.0")
    @SignIgnore
    public String updateShowDialog() {
    	//获取当前登录的会员
    	Member member = MemberUtils.getMember();
    	member.setIsShowDialog("1");
    	memberService.update(member, new EntityWrapper<Member>().eq("ali_user_id",member.getAliUserId()));
    	return "success";
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

}

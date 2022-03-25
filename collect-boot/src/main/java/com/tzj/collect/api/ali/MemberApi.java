package com.tzj.collect.api.ali;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.tzj.collect.common.util.DesUtil;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.service.MemberAliAccountService;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
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
	@Resource
	private MemberAliAccountService memberAliAccountService;

	private static Logger logger = LoggerFactory.getLogger(MemberApi.class);

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
	 * 根据用户授权返回的authCode,闲鱼授权解析接口
	 * @author 王灿
	 * @param
	 */
	@Api(name = "member.getXyAuthCode", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getXyAuthCode(MemberBean memberBean) {
		return memberService.getXyAuthCode(memberBean.getAuthCode());
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
	@SignIgnore
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
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object saveChannel(MemberBean memberBean) {
		Member member = MemberUtils.getMember();
		return memberService.saveChannelId(member.getAliUserId(),memberBean.getChannelId());
	}

	@Api(name = "member.form.id", version = "1.0")
	@SignIgnore
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
	/**
	 * 闲鱼用户更新支付宝账号
	 */
	@Api(name = "member.updateAliAccount", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object updateAliAccount(MemberBean memberBean) {
		String aliUserId = MemberUtils.getMember().getAliUserId();
		return memberAliAccountService.saveAliAccount(memberBean.getAliAccount(),aliUserId);

	}
	/**
	 * 根据Id删除支付宝账号
	 */
	@Api(name = "member.deleteAliAccountById", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object deleteAliAccountById(MemberBean memberBean) {
		String aliUserId = MemberUtils.getMember().getAliUserId();
		return memberAliAccountService.deleteAliAccountById(memberBean.getId(),aliUserId);

	}
	/**
	 * 根据查询该用户得支付宝账号列表
	 */
	@Api(name = "member.getAliAccountList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getAliAccountList() {
		String aliUserId = MemberUtils.getMember().getAliUserId();
		return memberAliAccountService.getAliAccountList(aliUserId);

	}
	/**
	 * 招行解析用户信息
	 */
	@Api(name = "member.getMemberMessage", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getMemberMessage(MemberBean memberBean) throws Exception {
		if(StringUtils.isBlank(memberBean.getMessage())||StringUtils.isBlank(memberBean.getTimestamp())){
			throw new RuntimeException("参数不可为空");
		}
		//令牌时间是否过期
		Long startTime = Long.parseLong(memberBean.getTimestamp());
		Long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;
		if (diff>30*60*1000){
			return "当前令牌失效，提示用户重新登录";
		}
		String s = null;
		try {
			s = DesUtil.decrypt(memberBean.getMessage());   //解析用户信息
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("用户信息解析失败");
		}
		//拿到key的地址
		/*URI resource = MemberBean.class.getClassLoader().getResource("key/public.key").toURI();
		String str2 = new File(resource).getAbsolutePath();*/
		//String path = Thread.currentThread().getContextClassLoader().getResource("key/public.key").getPath();
		String str = System.getProperty("user.dir");
		String str1 = str +"/key/public.key";
		if( null!=s){
			JSONObject json = JSONObject.parseObject(s, Feature.OrderedField);
			//转码拼接字符串，api签名验证
			String t1 = new BASE64Encoder().encode(json.get("data").toString().getBytes());
			String t = t1.replaceAll("\r|\n", "");
			String strBody = json.get("verify").toString();
			String signData=t+"&signature="+strBody;
			boolean flag = false;
			flag = DesUtil.VerifySignature(signData,str1);
			/*return json;*/
			//将解析的客户信息存入会员表
			if(flag==true){
				Member member = new Member();
				if(json.getJSONObject("data").getJSONObject("customerInfo").get("realName")!=null){
					String name = json.getJSONObject("data").getJSONObject("customerInfo").get("realName").toString();
					member.setLinkName(name);
				}
				if(json.getJSONObject("data").getJSONObject("customerInfo").get("mobile2")!=null){
					String tel  = json.getJSONObject("data").getJSONObject("customerInfo").get("mobile2").toString();
					member.setMobile(tel);
				}
				if(null!=member.getLinkName()||null!=member.getMobile()){
					memberService.insert(member);
				}
				return true;
			}else{
				logger.info("验证签名为false");
			}
		}
		return false;
	}
	/**
	 * 获取用户实体卡
	 * @param memberBean
	 * @return
	 */
	@Api(name = "member.getRealNo", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object getRealNo(MemberBean memberBean){
		if(org.apache.commons.lang3.StringUtils.isBlank(memberBean.getAliUserId())){
			return "未传aliUserId";
		}
		return memberService.getRealNoByAliUserId(memberBean.getAliUserId());
	}

	/**
	 * 根据用户的code,获取微信用户的信息
	 * @author 王灿
	 * @param
	 */
	@Api(name = "member.saveWxMsgByCode", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public Object saveWxMsgByCode(MemberBean memberBean) {
		return memberService.saveWxMsgByCode(memberBean);
	}

	/**
	 * 校验用户的手机号
	 * @author 王灿
	 * @param

	@Api(name = "member.updateMemberTel", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object updateMemberTel(MemberBean memberBean) {
		Member member = MemberUtils.getMember();
		return memberService.updateMemberTel(member.getAliUserId(),memberBean.getMobile(),memberBean.getCaptcha(),memberBean.getNetNo());
	}
	 */
}

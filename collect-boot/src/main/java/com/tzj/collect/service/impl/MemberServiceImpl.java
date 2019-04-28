package com.tzj.collect.service.impl;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.redis.RedisUtil;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.MemberMapper;
import com.tzj.collect.service.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 会员ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService{
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private PointService pointService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private PiccOrderService piccOrderService;
	@Autowired
	private PiccWaterService piccWaterService;
	@Autowired
	private PiccInsurancePolicyService piccInsurancePolicyService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private CompanyService companyService;

    @Override
    public Member findMemberByAliId(String aliMemberId) {
        return selectOne(new EntityWrapper<Member>().eq("ali_user_id", aliMemberId));
    }

    @Transactional
    @Override
    public Member saveByMemberBean(MemberBean memberBean) {
        Member member=new Member();
        member.setAliUserId(memberBean.getAliMemberId());
        member.setGreenCode(memberBean.getGreenSn());
        member.setIdCard(memberBean.getCertNo());
        member.setName(memberBean.getUserName());
        this.insert(member);
        return member;
    }
    /**
     * 根据用户的code解析用户数据并保存
     * @author 王灿
     * @param authCode
     * @return
     */ 
    @Transactional
	@Override
	public Object getAuthCode(String authCode,String state,String cityName,String source) {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
		System.out.println("--------拿到的参数state是："+state+"---拿到的cityName参数是 ： "+cityName);
		Area area = null;
		try {
			//根据拿到的state参数去查询他的主键
			area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", CityType.getEnum(state).getNameCN()));
			//并向前端返回行政市的id
			resultMap.put("cityId", area.getId());
			resultMap.put("cityName", area.getAreaName());
			resultMap.put("isExist","0");
			cityName = CityType.getEnum(state).getNameCN();
		} catch (Exception e) {
			try {
				//根据拿到的state参数去查询他的主键
				area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", cityName));
				resultMap.put("cityId", area.getId());
				resultMap.put("cityName", area.getAreaName());
				resultMap.put("isExist","0");
			} catch (Exception e2) {
				//根据拿到的state参数去查询他的主键
				area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", "上海市"));
				resultMap.put("cityId", area.getId());
				resultMap.put("cityName", area.getAreaName());
				resultMap.put("isExist","1");
			}
		}
		String appId = "";
		if (StringUtils.isBlank(source)){
			source = "H5";
		}
		if ("XCX".equals(source)){
			appId = AlipayConst.XappId;
		}else {
			appId = AlipayConst.appId;
		}

    		//根据用户授权的具体authCode查询是用户的userid和token
			AlipaySystemOauthTokenResponse  response = aliPayService.selectUserToken(authCode,appId);
			if(!response.isSuccess()){
				return "用户授权解析失败";
			} 
			String accessToken = response.getAccessToken();
			String userId = response.getUserId();
		AlipayUserInfoShareResponse userResponse = null;
		if ("XCX".equals(source)){
			userResponse= aliPayService.selectUser(accessToken,appId);
			if(!userResponse.isSuccess()){
			return "用户授权信息解析失败";
			}
		}
		//调用接口查询用户的详细信息
//		AlipayUserInfoShareResponse userResponse= aliPayService.selectUser(accessToken,appId);
//		if(!userResponse.isSuccess()){
//			return "用户授权信息解析失败";
//		}
		//查询用户是否存在
		Member member = this.selectOne(new EntityWrapper<Member>().eq("ali_user_id", userId));
		//外部会员卡号
		String cardNo = new SimpleDateFormat("yyyyMMdd").format(new Date())+ "000" +String.valueOf((int)((Math.random()*9+1)*100000)) ;
		//用户会员卡号(阿里返回)
		String aliCardNo=null;
		//用户会员开卡时间
		Date openCardDate=null;
		if(member == null) {
			UUID uuid = UUID.randomUUID();
			Object obj = null;
			try {
				obj = redisUtil.get(userId);
			}catch (Exception e){
				e.printStackTrace();
			}
			if (obj != null){
				throw new ApiException("用户已注册");
			}
			try {
				redisUtil.set(userId,uuid,10);
				Thread.sleep(100);
			}catch (Exception e){
				e.printStackTrace();
			}
			try {
				obj = redisUtil.get(userId);
			}catch (Exception e){
				e.printStackTrace();
			}
			if (!(obj+"").equals(uuid+"")){
				throw new ApiException("用户已注册");
			}
			member = new Member();
			member.setAliUserId(userId);
			if ("XCX".equals(source)){
				member.setName(userResponse.getUserName());
				member.setMobile(userResponse.getMobile());
				member.setBirthday(userResponse.getPersonBirthday());
				member.setGender(userResponse.getGender());
				member.setIsCertified(userResponse.getIsCertified());
				member.setCity(userResponse.getCity());
				member.setLinkName(userResponse.getNickName());
				member.setPicUrl(userResponse.getAvatar());
			}
			member.setAddress(cityName);
			//给用户发放会员卡
			Map<String, Object> map = aliPayService.send(accessToken, userId, cardNo, "0", AlipayConst.template_id, "0", null,appId);
			aliCardNo = map.get("bizCardNo")==null?null:map.get("bizCardNo").toString();
			openCardDate = (Date) map.get("openDate");
			member.setAliCardNo(aliCardNo);
			try {
				member.setOpenCardDate(openCardDate==null?openCardDate:new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			member.setCardNo(cardNo);
			member.setAppId(appId);
			this.insert(member);
		}else {
			member.setAliUserId(userId);
			if ("XCX".equals(source)){
				member.setName(userResponse.getUserName());
				member.setMobile(userResponse.getMobile());
				member.setBirthday(userResponse.getPersonBirthday());
				member.setGender(userResponse.getGender());
				member.setIsCertified(userResponse.getIsCertified());
				member.setCity(userResponse.getCity());
				member.setLinkName(userResponse.getNickName());
				member.setPicUrl(userResponse.getAvatar());
			}
//			member.setIdCard(userResponse.getCertNo());
//			member.setName(userResponse.getUserName());
//			if(StringUtils.isNotBlank(userResponse.getMobile())) {
//				member.setMobile(userResponse.getMobile());
//			}
			member.setAddress(cityName);
			//判断是否给用户发过会员卡
			if(StringUtils.isBlank(member.getAliCardNo())) {
				//获取用户积分数据
				Point piont = pointService.getPoint(member.getId());
				String points = "0";
				if(piont!=null) {
					points = piont.getPoint()+"";
				}
				//给用户发放会员卡
				Map<String, Object> map = aliPayService.send(accessToken, userId, cardNo, points, AlipayConst.template_id, "0", null,appId);
				aliCardNo = map.get("bizCardNo")==null?null:map.get("bizCardNo").toString();
				openCardDate = (Date)map.get("openDate");
				member.setAliCardNo(aliCardNo);
				try {
					member.setOpenCardDate(openCardDate==null?openCardDate:new Date());
				} catch (Exception e) {
					e.printStackTrace();
				}
				member.setCardNo(cardNo);
				member.setAppId(appId);
			}
			this.updateById(member);
		}
		
		resultMap.put("id", member.getId());
		if(StringUtils.isNotBlank(member.getMobile())||"XCX".equals(source)) {
			resultMap.put("mobile", "1");
			String token= JwtUtils.generateToken(member.getId().toString(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
			String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
			System.out.println("token:"+securityToken);
			resultMap.put("member", member);
			resultMap.put("token", securityToken);
			resultMap.put("isShowDialog", member.getIsShowDialog());
		}else {
			resultMap.put("mobile", "0");
		}
		return resultMap;
    			
	}
    /**
     * 根据用户授权返回的authCode,获取用户的token
     * @author 王灿
     * @param authCode : 用户授权返回的Code
     * @return
     */
	@Override
	public Object getUserToken(String authCode,String cityName) {
		//根据用户授权的具体authCode查询是用户的userid和token 
		AlipaySystemOauthTokenResponse  response = aliPayService.selectUserToken(authCode, AlipayConst.appId);
		if(!response.isSuccess()){
			return "用户授权解析失败";
		} 
		String accessToken = response.getAccessToken();
		String userId = response.getUserId();
		//调用接口查询用户的详细信息
		AlipayUserInfoShareResponse userResponse= aliPayService.selectUser(accessToken, AlipayConst.appId);
		if(!userResponse.isSuccess()){
			return "用户授权信息解析失败";
		}
		//查询用户是否存在
		Member member = this.selectOne(new EntityWrapper<Member>().eq("ali_user_id", userId));
		if(member==null) {
			return "系统目前暂无此用户";
		}
		Map<String,Object> map = new HashMap<String,Object>();
		String token= JwtUtils.generateToken(member.getId().toString(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
		String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
		System.out.println("用户的token是:"+securityToken);
			Area area = null;
			try {
				//根据拿到的state参数去查询他的主键
				area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", cityName));
				map.put("cityId", area.getId());
			} catch (Exception e2) {
				//根据拿到的state参数去查询他的主键
				area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", "上海市"));
				map.put("cityId", area.getId());
			}
			map.put("id", member.getId());
			map.put("token", securityToken);
		return map;
	}

	/**
	 * 获取会员个人中心的相关数据
	 * @author 王灿
	 * @param
	 */
	@Override
	public Object memberAdmin(Integer memberId) {
		Map<String,Object> resultMap = new HashMap<>();
		String isPiccInsurance = "NO";
		String isPiccWater = "NO";
		Double greenCount = 0.0;
		Integer insuranceId = null;
		String title = null;
		String defeatMsg = "";
		//查询该用户是否有保单
		PiccOrder piccOrder = piccOrderService.selectOne(new EntityWrapper<PiccOrder>().eq("member_id", memberId).eq("del_flag", 0).in("status_","0,2,4"));
		if (null != piccOrder){
			if(piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.OPENING.getValue()){
				isPiccInsurance = "YES";
			}else if(piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.RECEIVE.getValue()||piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.WAIT.getValue()){
				isPiccInsurance = "ING";
			}
		}else{
			List<PiccOrder> piccOrderList = piccOrderService.selectList(new EntityWrapper<PiccOrder>().eq("member_id", memberId).eq("del_flag", 0).eq("status_", "1").orderBy("create_date", false));
			if(null != piccOrderList&&!piccOrderList.isEmpty()){
				defeatMsg = piccOrderList.get(0).getCancelReason();
			}
		}
		//判断是否有保额待领取
		PiccWater piccWater = piccWaterService.selectOne(new EntityWrapper<PiccWater>().eq("member_id", memberId).eq("del_flag", 0).eq("status_", 0));
		if(null!= piccWater){
			isPiccWater = "YES";
		}
		//查询保单信息
		List<PiccInsurancePolicy> piccInsurancePolicy = piccInsurancePolicyService.selectList(new EntityWrapper<PiccInsurancePolicy>().eq("del_flag", 0));
		if(null!=piccInsurancePolicy){
			insuranceId = piccInsurancePolicy.get(0).getId().intValue();
			title = piccInsurancePolicy.get(0).getTitle();
		}
		//查询用户的绿色能量
		Point point = pointService.selectOne(new EntityWrapper<Point>().eq("member_id", memberId).eq("del_flag", 0));

		Member member = this.selectById(memberId);
		if ( null != point){
			greenCount = point.getPoint();
			resultMap.put("greenCount",greenCount);
		}else {
			resultMap.put("greenCount","0");
		}
		//获取个人中心电话
		Company company = companyService.selectById(1);
		resultMap.put("isPiccInsurance",isPiccInsurance);
		resultMap.put("isPiccWater",isPiccWater);
		resultMap.put("piccOrder",piccOrder);
		resultMap.put("insuranceId",insuranceId);
		resultMap.put("title",title);
		resultMap.put("piccOrder",piccOrder);
		resultMap.put("member",member);
		resultMap.put("defeatMsg",defeatMsg);
		resultMap.put("tel",company.getTel());
		return resultMap;
	}

	@Override
	public Map<String, Object> memberIsExist(MemberBean memberBean) {
		Map<String, Object> map = new HashMap<>();
		Member member = this.selectOne(new EntityWrapper<Member>().eq("card_no", memberBean.getCardNo()));
		if (member != null) {
			map.put("isExist", true);
			map.put("tel", member.getMobile());
			return map;
		}else {
			map.put("isExist", false);
			return map;
		}
	}

	public static void main(String[] args) throws  Exception{

		Thread.sleep(100);
		System.out.println(PiccOrder.PiccOrderType.RECEIVE.getValue());
	}

	/**
	 *  小程序静默授权
	 * 根据用户授权返回的authCode,获取用户的token
	 * @author 王灿
	 * @param authCode : 用户授权返回的Code
	 * @return
	 */
	@Transactional
	@Override
	public Object getStaticUserToken(String authCode,String cityName) {
		System.out.println("--------拿到的参数authCode是："+authCode+"---拿到的cityName参数是 ： "+cityName);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Area area = null;
		try {
			//根据拿到的state参数去查询他的主键
			area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", cityName));
			resultMap.put("cityId", area.getId());
			resultMap.put("cityName", area.getAreaName());
			resultMap.put("isExist","0");
		} catch (Exception e2) {
			//根据拿到的state参数去查询他的主键
			area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", "上海市"));
			resultMap.put("cityId", area.getId());
			resultMap.put("cityName", area.getAreaName());
			resultMap.put("isExist","1");
		}
		String appId = AlipayConst.XappId;
		//根据用户授权的具体authCode查询是用户的userid和token
		AlipaySystemOauthTokenResponse  response = aliPayService.selectUserToken(authCode,appId);
		if(!response.isSuccess()){
			return "用户授权解析失败";
		}
		String accessToken = response.getAccessToken();
		String userId = response.getUserId();
		//查询用户是否存在
		Member member = this.selectOne(new EntityWrapper<Member>().eq("ali_user_id", userId));
		if(member==null||StringUtils.isBlank(member.getAliCardNo())||StringUtils.isBlank(member.getLinkName())){
			resultMap.put("token",null);
			return resultMap;
		}
		if(StringUtils.isBlank(member.getName())||StringUtils.isBlank(member.getMobile())){
			resultMap.put("token",null);
			return resultMap;
		}
		String token= JwtUtils.generateToken(member.getId().toString(), ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
		String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
		System.out.println("token:"+securityToken);
		resultMap.put("token", securityToken);
		//更新会员登录时间
		member.setUpdateDate(new Date());
		this.updateById(member);
		resultMap.put("member",member);
		return resultMap;
	}
	
}

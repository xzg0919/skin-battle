package com.tzj.collect.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayMarketingCardQueryResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.mqtt.util.Tools;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.shard.ShardTableHelper;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.mapper.DsddMemberMapper;
import com.tzj.collect.core.mapper.MemberMapper;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.thread.NewThreadPoorExcutor;
import com.tzj.collect.core.utils.TableNameUtils;
import com.tzj.collect.entity.*;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_EXPRIRE;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;

import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

/**
 * 会员ServiceImpl
 *
 * @Author 王灿
 */
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberXianyuService memberXianyuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PointListService pointListService;
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
    @Autowired
    private VoucherMemberService voucherMemberService;

    @Override
    public Member findMemberByAliId(String aliMemberId) {
        return this.selectMemberByAliUserId(aliMemberId);
    }

    @Transactional
    @Override
    public Member saveByMemberBean(MemberBean memberBean) {
        Member member = new Member();
        member.setAliUserId(memberBean.getAliMemberId());
        member.setGreenCode(memberBean.getGreenSn());
        member.setIdCard(memberBean.getCertNo());
        member.setName(memberBean.getUserName());
        member.setCardNo(ToolUtils.getIdCardByAliUserId(memberBean.getAliMemberId()));
        this.insertMember(member);
        return member;
    }

    /**
     * 根据用户的code解析用户数据并保存
     *
     * @param authCode
     * @return
     * @author 王灿
     */
    @Transactional
    @Override
    public Object getAuthCode(String authCode, String state, String cityName, String source) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        System.out.println("--------拿到的参数state是：" + state + "---拿到的cityName参数是 ： " + cityName);
        Area area = null;
        try {
            //根据拿到的state参数去查询他的主键
            area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", CityType.getEnum(state).getNameCN()));
            //并向前端返回行政市的id
            resultMap.put("cityId", area.getId());
            resultMap.put("cityName", area.getAreaName());
            resultMap.put("isExist", "0");
            cityName = CityType.getEnum(state).getNameCN();
        } catch (Exception e) {
            try {
                //根据拿到的state参数去查询他的主键
                area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", cityName));
                resultMap.put("cityId", area.getId());
                resultMap.put("cityName", area.getAreaName());
                resultMap.put("isExist", "0");
            } catch (Exception e2) {
                //根据拿到的state参数去查询他的主键
                area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", "上海市"));
                resultMap.put("cityId", area.getId());
                resultMap.put("cityName", area.getAreaName());
                resultMap.put("isExist", "1");
            }
        }
        String appId = "";
        if (StringUtils.isBlank(source)) {
            source = "H5";
        }
        if ("XCX".equals(source)) {
            appId = AlipayConst.XappId;
        } else {
            appId = AlipayConst.appId;
        }

        //根据用户授权的具体authCode查询是用户的userid和token
        AlipaySystemOauthTokenResponse response = aliPayService.selectUserToken(authCode, appId);
        if (!response.isSuccess()) {
            return "用户授权解析失败";
        }
        String accessToken = response.getAccessToken();
        String userId = response.getUserId();
        AlipayUserInfoShareResponse userResponse = null;
        if ("XCX".equals(source)) {
            userResponse = aliPayService.selectUser(accessToken, appId);
            if (!userResponse.isSuccess()) {
                return "用户授权信息解析失败";
            }
        }
        //查询用户是否存在
        Member member = this.selectMemberByAliUserId(userId);
        //外部会员卡号
        String cardNo = ToolUtils.getIdCardByAliUserId(userId);
        //用户会员卡号(阿里返回)
        String aliCardNo = null;
        //用户会员开卡时间
        Date openCardDate = null;
        if (member == null) {
            member = new Member();
            member.setAliUserId(userId);
            if ("XCX".equals(source)) {
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
            Map<String, Object> map = aliPayService.send(accessToken, userId, cardNo, "0", AlipayConst.template_id, "0", null, appId);
            aliCardNo = map.get("bizCardNo") == null ? null : map.get("bizCardNo").toString();
            openCardDate = (Date) map.get("openDate");
            member.setAliCardNo(aliCardNo);
            try {
                member.setOpenCardDate(openCardDate == null ? openCardDate : new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
            member.setCardNo(cardNo);
            member.setAppId(appId);
            this.insertMember(member);
            //定时定点用户进来更新同步收呗用户信息（step:1 将定时定点实体卡绑到收呗用户表中 ，2 删除定时定点用户 3 订单、积分、积分流水表中相应添加aliuserId 和 card_no）
//            try {
//                this.operateDsddMember(member);
//            } catch (Exception e) {
//                System.out.println("实体卡换卡出错了" + JSONObject.toJSONString(member));
//            }
            try {
                voucherMemberService.reSend(member.getAliUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            member.setAliUserId(userId);
            if ("XCX".equals(source)) {
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
            //判断是否给用户发过会员卡
            if (StringUtils.isBlank(member.getAliCardNo()) || !userId.equals(ToolUtils.getAliUserIdByOrderNo(member.getCardNo()))) {
                //获取用户积分数据
                Point piont = pointService.getPoint(member.getAliUserId());
                String points = "0";
                if (piont != null) {
                    points = piont.getPoint() + "";
                }
                //给用户发放会员卡
                Map<String, Object> map = aliPayService.send(accessToken, userId, cardNo, points, AlipayConst.template_id, "0", null, appId);
                aliCardNo = map.get("bizCardNo") == null ? null : map.get("bizCardNo").toString();
                openCardDate = (Date) map.get("openDate");
                member.setAliCardNo(aliCardNo);
                try {
                    member.setOpenCardDate(openCardDate == null ? openCardDate : new Date());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                member.setCardNo(cardNo);
                member.setAppId(appId);
            }
            this.updateMemberByAliUserId(member);
        }

        //判断是否是定时定点用户表中存在该手机号
//        DsddMember dsddMember = dsddMemberService.selectOne(new EntityWrapper<DsddMember>().eq("mobile", userResponse.getMobile()));
//        try {
//            if (dsddMember != null){
//                dsddMemberService.update(dsddMember,new EntityWrapper<DsddMember>().eq("del_flag","1"));
//                orderService.updateForSet("ali_user_id = '" + userId + "'",new EntityWrapper<Order>().eq("tel", userResponse.getMobile()));
//                pointService.updateForSet("ali_user_id = " + userId + "'", new EntityWrapper<Point>().eq("telephone", userResponse.getMobile()));
//                pointListService.updateForSet("telephone='',ali_user_id='" + userId + "'", new EntityWrapper<PointList>().eq("telephone", userResponse.getMobile()));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //定时定点用户进来更新同步收呗用户信息（step:1 将定时定点实体卡绑到收呗用户表中 ，2 删除定时定点用户 3 订单、积分、积分流水表中相应添加aliuserId 和 card_no）
        try {
            Member finalMember = member;
            NewThreadPoorExcutor.getThreadPoor().execute(new Thread(()->{
                this.operateDsddMember(finalMember);
            }));
        } catch (Exception e) {
            System.out.println("实体卡换卡出错了" + JSONObject.toJSONString(member));
        }
        resultMap.put("id", member.getAliUserId());
        if (StringUtils.isNotBlank(member.getMobile()) || "XCX".equals(source)) {
            resultMap.put("mobile", "1");
            String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
            System.out.println("token:" + securityToken);
            resultMap.put("member", member);
            resultMap.put("token", securityToken);
            resultMap.put("isShowDialog", member.getIsShowDialog());
        } else {
            resultMap.put("mobile", "0");
        }
        return resultMap;

    }

    /**
     * 操作定时定点用户表（1. 将定时定点用户表信息【定时定点卡号 、开卡时间】移至收呗用户表中， 2 删除定时定点用户表 3 订单、积分、积分流水表中相应添加aliuserId 和 card_no
     *
     * @param member
     */
    @Transactional
    public void operateDsddMember(Member member) {
        if (StringUtils.isNotBlank(member.getMobile())) {
            //1. 将定时定点用户表信息【定时定点卡号 、开卡时间】移至收呗用户表中，
            this.baseMapper.updateMemberFromDsdd(TableNameUtils.getMemberTableName(member), member.getMobile());
            //2 删除定时定点用户表
            this.baseMapper.deleteDsddMember(member.getMobile());
            //3 订单、积分、积分流水表中相应添加aliuserId 和 card_no
            pointListService.updatePointAndOrderFromDsdd(member.getAliUserId(), member.getMobile(), member.getCardNo());
        }
    }

    /**
     * 根据用户授权返回的authCode,获取用户的token
     *
     * @param authCode : 用户授权返回的Code
     * @return
     * @author 王灿
     */
    @Override
    public Object getUserToken(String authCode, String cityName) {
        //根据用户授权的具体authCode查询是用户的userid和token 
        AlipaySystemOauthTokenResponse response = aliPayService.selectUserToken(authCode, AlipayConst.appId);
        if (!response.isSuccess()) {
            return "用户授权解析失败";
        }
        String accessToken = response.getAccessToken();
        String userId = response.getUserId();
        //调用接口查询用户的详细信息
        AlipayUserInfoShareResponse userResponse = aliPayService.selectUser(accessToken, AlipayConst.appId);
        if (!userResponse.isSuccess()) {
            return "用户授权信息解析失败";
        }
        //查询用户是否存在
        Member member = this.selectMemberByAliUserId(userId);
        if (member == null) {
            return "系统目前暂无此用户";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        System.out.println("用户的token是:" + securityToken);
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
     *
     * @param
     * @author 王灿
     */
    @Override
    public Object memberAdmin(String aliUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        String isPiccInsurance = "NO";
        String isPiccWater = "NO";
        Double greenCount = 0.0;
        Integer insuranceId = null;
        String title = null;
        String defeatMsg = "";
        //查询该用户是否有保单
        PiccOrder piccOrder = piccOrderService.selectOne(new EntityWrapper<PiccOrder>().eq("ali_user_id", aliUserId).eq("del_flag", 0).in("status_", "0,2,4"));
        if (null != piccOrder) {
            if (piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.OPENING.getValue()) {
                isPiccInsurance = "YES";
            } else if (piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.RECEIVE.getValue() || piccOrder.getStatus().getValue() == PiccOrder.PiccOrderType.WAIT.getValue()) {
                isPiccInsurance = "ING";
            }
        } else {
            List<PiccOrder> piccOrderList = piccOrderService.selectList(new EntityWrapper<PiccOrder>().eq("ali_user_id", aliUserId).eq("del_flag", 0).eq("status_", "1").orderBy("create_date", false));
            if (null != piccOrderList && !piccOrderList.isEmpty()) {
                defeatMsg = piccOrderList.get(0).getCancelReason();
            }
        }
        //判断是否有保额待领取
        PiccWater piccWater = piccWaterService.selectOne(new EntityWrapper<PiccWater>().eq("ali_user_id", aliUserId).eq("del_flag", 0).eq("status_", 0).ge("point_count", 1));
        if (null != piccWater) {
            isPiccWater = "YES";
        }
        //查询保单信息
        List<PiccInsurancePolicy> piccInsurancePolicy = piccInsurancePolicyService.selectList(new EntityWrapper<PiccInsurancePolicy>().eq("del_flag", 0));
        if (null != piccInsurancePolicy) {
            insuranceId = piccInsurancePolicy.get(0).getId().intValue();
            title = piccInsurancePolicy.get(0).getTitle();
        }
        //查询用户的绿色能量
        Point point = pointService.selectOne(new EntityWrapper<Point>().eq("ali_user_id", aliUserId).eq("del_flag", 0));
        Member member = this.selectMemberByAliUserId(aliUserId);
        if (null != point) {
            greenCount = point.getPoint();
            resultMap.put("greenCount", greenCount);
            resultMap.put("remainPoint", point.getRemainPoint());
        } else {
            resultMap.put("greenCount", "0");
            resultMap.put("remainPoint", "0");
        }
        //获取个人中心电话
        Company company = companyService.selectById(1);
        resultMap.put("isPiccInsurance", isPiccInsurance);
        resultMap.put("isPiccWater", isPiccWater);
        resultMap.put("piccOrder", piccOrder);
        resultMap.put("insuranceId", insuranceId);
        resultMap.put("title", title);
        resultMap.put("piccOrder", piccOrder);
        resultMap.put("member", member);
        resultMap.put("defeatMsg", defeatMsg);
        resultMap.put("tel", company.getTel());
        return resultMap;
    }

    @Override
    public Map<String, Object> memberIsExist(MemberBean memberBean) {
        Map<String, Object> map = new HashMap<>();
        String aliUid = ToolUtils.getAliUserIdByOrderNo(memberBean.getCardNo());
        Member member = this.selectMemberByAliUserId(aliUid);
        if (member != null) {
            map.put("isExist", true);
            map.put("tel", member.getMobile());
            return map;
        } else {
            map.put("isExist", false);
            return map;
        }
    }

    /**
     * 小程序静默授权 根据用户授权返回的authCode,获取用户的token
     *
     * @param authCode : 用户授权返回的Code
     * @return
     * @author 王灿
     */
    @Transactional
    @Override
    public Object getStaticUserToken(String authCode, String cityName) {
        System.out.println("--------拿到的参数authCode是：" + authCode + "---拿到的cityName参数是 ： " + cityName);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Area area = null;
        try {
            //根据拿到的state参数去查询他的主键
            area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", cityName));
            resultMap.put("cityId", area.getId());
            resultMap.put("cityName", area.getAreaName());
            resultMap.put("isExist", "0");
        } catch (Exception e2) {
            //根据拿到的state参数去查询他的主键
            area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", "上海市"));
            resultMap.put("cityId", area.getId());
            resultMap.put("cityName", area.getAreaName());
            resultMap.put("isExist", "1");
        }
        String appId = AlipayConst.XappId;
        //根据用户授权的具体authCode查询是用户的userid和token
        AlipaySystemOauthTokenResponse response = aliPayService.selectUserToken(authCode, appId);
        if (!response.isSuccess()) {
            return "用户授权解析失败";
        }
        String accessToken = response.getAccessToken();
        String userId = response.getUserId();
        //查询用户是否存在
        Member member = this.selectMemberByAliUserId(userId);
        if (member == null || StringUtils.isBlank(member.getAliCardNo())) {
            resultMap.put("token", null);
            return resultMap;
        }
        ;
        if (!userId.equals(ToolUtils.getAliUserIdByOrderNo(member.getCardNo()))) {
            //删除会员卡
            aliPayService.deleteCard(member);
            resultMap.put("token", null);
            return resultMap;
        }
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        System.out.println("token:" + securityToken);
        resultMap.put("token", securityToken);
        //更新会员登录时间
        member.setUpdateDate(new Date());
        this.updateMemberByAliUserId(member);
        resultMap.put("member", member);
        return resultMap;
    }

    @Override
    public Object getPassIdUrl(String aliUserId) {
        Member member = this.selectMemberByAliUserId(aliUserId);
        AlipayMarketingCardQueryResponse response = aliPayService.getPassIdUrl(member.getAliCardNo(), member.getAliUserId());
        Map<String, Object> resultMap = new HashMap<>();
        if (response.isSuccess()) {
            resultMap.put("passId", response.getPassId() == null ? "No" : response.getPassId());
            resultMap.put("schemaUrl", response.getSchemaUrl() == null ? "No" : response.getSchemaUrl());
        } else {
            resultMap.put("passId", "No");
            resultMap.put("schemaUrl", "No");
        }
        return resultMap;
    }

    @Override
    public Object userToken(String authCode) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String appId = AlipayConst.flcxaAppId;
        //根据用户授权的具体authCode查询是用户的userid和token
        AlipaySystemOauthTokenResponse response = aliPayService.flcxToken(authCode, appId);
        if (!response.isSuccess()) {
            return "用户授权解析失败";
        }
        String userId = response.getUserId();
        resultMap.put("aliUserId", userId);
        return resultMap;
    }

    @Override
    @Transactional
    public String saveChannelId(String aliUserId, String channelId) {
        Member member = this.selectMemberByAliUserId(aliUserId);
        if (null != member) {
            member.setChannelId(channelId);
            this.updateMemberByAliUserId(member);
        }
        return "操作成功";
    }

    @Override
    public long getMemberCount() {
        return memberMapper.getMemberCount();
    }

    @Override
    public long getMemberCountToDay() {
        return memberMapper.getMemberCountToDay();
    }

    @Override
    public Map<String, Object> selectMemberInfoByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", Long.parseLong(aliUserId), 40);
        return memberMapper.selectMemberInfoByAliUserId(aliUserId, memberName);
    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> updateUserFormId(Member member) {
        Map<String, Object> returnMap = new HashMap<>();
        if (this.updateMemberByAliUserId(member) >= 1) {
            returnMap.put("isSuccess", "Y");
        } else {
            returnMap.put("isSuccess", "N");
        }
        return returnMap;
    }

    /**
     * 获取用户积分（包括积分商户）
     *
     * @param aliUserId
     * @return
     */
    @Override
    public Object getAllPoints(String aliUserId) {
        //查询用户的绿色能量
        Point point = pointService.selectOne(new EntityWrapper<Point>().eq("ali_user_id", aliUserId).eq("del_flag", 0));
        Member member = this.selectMemberByAliUserId(aliUserId);
        Map<String, Object> resultMap = new HashMap<>(2);
        Map<String, String> params = new HashMap<>(2);
        //{"name":"listForSupport","version":"1.0","nonce":1578972462692.6304,"timestamp":1578972462692}
        params.put("name", "member.getAllPoints");
        params.put("version", "1.0");
        params.put("nonce", String.valueOf(new Date().getTime()));
        params.put("timestamp", String.valueOf(new Date().getTime()));
        JSONObject al = new JSONObject();
        al.put("aliUserId", aliUserId);
        params.put("data", al.toJSONString());
        Double tatalPoints = 0.0;
        Double validPoints = 0.0;
        try {
            //JSONObject result = Tools.httpsPost("https://open.mayishoubei.com/company/api", params);
            JSONObject result = Tools.httpsPost("http://localhost:9080/company/api", params);
            if (result != null && "0".equals(result.get("code"))) {
                JSONObject data = (JSONObject) result.get("data");
                tatalPoints = (double) data.get("tatalPoints");
                validPoints = (double) data.get("validPoints");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Double greenCount = 0.0;
        Double remainPoint = 0.0;
        if (null != point) {
            greenCount = point.getPoint();
            remainPoint = point.getRemainPoint();
            BigDecimal x = new BigDecimal(greenCount);
            BigDecimal y = new BigDecimal(remainPoint);

            resultMap.put("greenCount", x.add(new BigDecimal(tatalPoints)).doubleValue());
            resultMap.put("remainPoint", y.add(new BigDecimal(validPoints)).doubleValue());
        } else {
            resultMap.put("greenCount", tatalPoints);
            resultMap.put("remainPoint", validPoints);
        }
        return resultMap;
    }

    @Override
    public Object getXyAuthCode(String authCode) {
        Map<String,Object> resultMap = new HashMap<>();
            String token = JwtUtils.generateToken("2088432503718960", ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
            System.out.println("token:" + securityToken);
            resultMap.put("token", securityToken);
            return resultMap;
    }

    @Override
    @Transactional
    public Object saveXianYuMember(String openId, String accessToken,String linkName,String picUrl) {
        //查询用户是否在闲鱼用户表内存在
        MemberXianyu memberXianyu = memberXianyuService.selectOne(new EntityWrapper<MemberXianyu>().eq("open_id", openId));
        if (null == memberXianyu){
            memberXianyu = new MemberXianyu();
        }
        memberXianyu.setLinkName(linkName);
        memberXianyu.setPicUrl(picUrl);
        memberXianyu.setOpenId(openId);
        memberXianyu.setAccessToken(accessToken);
        memberXianyuService.insertOrUpdate(memberXianyu);
        //查询用户在支付宝是否存在
        Member member = this.selectMemberByAliUserId(memberXianyu.getAliUserId());
        if (null == member){
            member = new Member();
        }
        member.setName(linkName);
        member.setIsCertified("1");
        member.setCity("闲鱼");
        member.setAliUserId(memberXianyu.getAliUserId());
        member.setLinkName(linkName);
        member.setPicUrl(picUrl);
        member.setAddress("闲鱼");
        member.setAliCardNo(memberXianyu.getCardNo());
        member.setCardNo(memberXianyu.getCardNo());
        member.setChannelId("1");
        this.inserOrUpdatetMember(member);
        Map<String,Object> resultMap = new HashMap<>();
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        System.out.println("token:" + securityToken);
        resultMap.put("member", member);
        resultMap.put("token", securityToken);
        return resultMap;

    }

    @Override
    @Transactional
    public Object updateAliAccount(String aliAccount, String aliUserId) {
        //查询用户在支付宝是否存在
        Member member = this.selectMemberByAliUserId(aliUserId);
        if (null == member){
           throw new ApiException("用户信息异常");
        }
        if ("1".equals(member.getChannelId())){
            //查询用户是否在闲鱼用户表内存在
            MemberXianyu memberXianyu = memberXianyuService.selectOne(new EntityWrapper<MemberXianyu>().eq("ali_user_id", aliUserId));
            if (null == memberXianyu){
                throw new ApiException("用户信息异常");
            }
            memberXianyu.setAliAccount(aliAccount);
            memberXianyuService.updateById(memberXianyu);
        }
        return "操作成功";
    }

    @Override
    public Member selectMemberByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", Long.parseLong(aliUserId), 40);
        return memberMapper.selectMemberByAliUserId(aliUserId, memberName);
    }

    @Override
    @Transactional
    public Integer deleteMemberByAliUserId(String aliUserId) {
        String memberName = ShardTableHelper.getTableNameByModeling("sb_member", Long.parseLong(aliUserId), 40);
        return memberMapper.deleteMemberByAliUserId(aliUserId, memberName);
    }

    @Override
    @Transactional
    public Integer updateMemberByAliUserId(Member member) {
        String memberTableName = TableNameUtils.getMemberTableName(member);
        member.setTableName(memberTableName);
        return memberMapper.updateMemberByAliUserId(member);
    }

    @Override
    @Transactional
    public Integer insertMember(Member member) {
        String memberTableName = TableNameUtils.getMemberTableName(member);
        member.setTableName(memberTableName);
        return memberMapper.insertMember(member);
    }

    @Override
    @Transactional
    public Integer inserOrUpdatetMember(Member member) {
        if (null==member.getId()) {
            return this.insertMember(member);
        } else {
            return this.updateMemberByAliUserId(member);
        }
    }

}

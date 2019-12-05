package com.tzj.iot.api.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.param.iot.SmilePayBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.service.AliPayService;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * @author sgmark
 * @create 2019-11-26 11:17
 **/
@ApiService
@ApiDoc(value = "APP iot 设备端ali模块",appModule = "ali")
public class IotAliApi {

    @Resource
    private AliPayService aliPayService;

    @Resource
    private MemberService memberService;

    /**
     * 人脸识别初始化
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/28 0028
     * @Param: 
     * @return: 
     */
    @Api(name = "smile.pay.initialize", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Object smilePayInitialize(SmilePayBean smilePayBean){
        if (null == smilePayBean || StringUtils.isEmpty(smilePayBean.getMetaInfo())){
            throw new ApiException("参数错误");
        }else {
            return JSONObject.parseObject(aliPayService.smilePayInitialize(smilePayBean.getMetaInfo()).getResult());
        }
    }
    /**
     * 人脸识别获取用户token
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/28 0028
     * @Param: 
     * @return: 
     */
    @Api(name = "smile.pay.ali.token", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public TokenBean  saveUserBySmileToken(SmilePayBean smilePayBean){
        ZolozAuthenticationCustomerFtokenQueryResponse zolozAuthenticationCustomerFtokenQueryResponse = aliPayService.customerFtokenQuery(smilePayBean.getFToken());
        if (!StringUtils.isEmpty(zolozAuthenticationCustomerFtokenQueryResponse.getUid())){
            MemberBean memberBean = new MemberBean();
            memberBean.setAliMemberId(zolozAuthenticationCustomerFtokenQueryResponse.getUid());
            TokenApi tokenApi = new TokenApi();
            //先根据 ali id 去查询，没有的话新增，有的话直接返回token
            Member member = memberService.findMemberByAliId(memberBean.getAliMemberId());
            if (member == null) {
                //新增一条记录
                member = memberService.saveByMemberBean(memberBean);
            }
            String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
            TokenBean tokenBean = new TokenBean();
            tokenBean.setExpire(ALI_API_EXPRIRE);
            tokenBean.setToken(securityToken);
            return tokenBean;
        }else {
            throw new ApiException(zolozAuthenticationCustomerFtokenQueryResponse.getSubMsg());
        }
    }
}

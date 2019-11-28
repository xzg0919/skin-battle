package com.tzj.iot.api.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.param.iot.SmilePayBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.service.AliPayService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * @author sgmark
 * @create 2019-11-26 11:17
 **/
public class IotAliApi {

    @Resource
    private AliPayService aliPayService;

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
        if (null == smilePayBean || StringUtils.isEmpty(smilePayBean.getApdidToken()) || StringUtils.isEmpty(smilePayBean.getAppName())
        || StringUtils.isEmpty(smilePayBean.getAppVersion()) || StringUtils.isEmpty(smilePayBean.getBioMetaInfo())){
            throw new ApiException("参数错误");
        }
       return JSONObject.parseObject(aliPayService.smilePayInitialize(smilePayBean.getApdidToken(), smilePayBean.getAppName(), smilePayBean.getAppVersion(), smilePayBean.getBioMetaInfo()).getResult());
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
            TokenBean token = tokenApi.getToken(memberBean);
            return token;
        }
        return null;
    }
}

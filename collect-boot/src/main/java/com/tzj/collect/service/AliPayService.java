package com.tzj.collect.service;

import java.util.Date;
import java.util.Map;

import com.alipay.api.response.*;

/**
 * @Author 王灿
 **/
public interface AliPayService {
	 /**
     * 根据用户授权的具体authCode查询是用户的userid和token 
     * @author 王灿
     * @param AuthCode
     * @return
     */
    public AlipaySystemOauthTokenResponse selectUserToken(String userCode,String appId);
    /**
     * 调用接口查询用户的详细信息
     * @author 王灿
     * @param AuthCode
     * @return
     */
    public AlipayUserInfoShareResponse selectUser(String userToken,String appId);
    /**
     * 
     * <p>Discription:[发放会员卡]</p>
     * @author:[王灿] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Map<String,Object>
     */
	public Map<String,Object> send(String accessToken,String userId,
	            String cardNo,String point,String templateId,String balance,String vip,String appId);
	/**
     * <p>Discription:[更改会员积分]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public String updatePoint(String targetCardNo, Date openDate, String point,String vip,String appId);
    /**
     * <p>芝麻认证初始化接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationInitializeResponse initialize(String certName, String certNo);
    /**
     * <p>芝麻认证初返回URL</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationCertifyResponse getInitializeUrl(String bizNo);
    /**
     * <p>芝麻认证初开始接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationQueryResponse certify(String bizNo);
}

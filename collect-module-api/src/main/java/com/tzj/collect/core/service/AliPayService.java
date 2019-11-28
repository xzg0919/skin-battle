package com.tzj.collect.core.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayUserCertifyOpenQueryRequest;
import com.alipay.api.response.*;
import com.tzj.collect.entity.Member;

import java.util.Date;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface AliPayService {
	 /**
     * 根据用户授权的具体authCode查询是用户的userid和token 
     * @author 王灿
     * @return
     */
    public AlipaySystemOauthTokenResponse selectUserToken(String userCode, String appId);
    /**
     * 调用接口查询用户的详细信息
     * @author 王灿
     * @return
     */
    public AlipayUserInfoShareResponse selectUser(String userToken, String appId);
    /**
     *
     * <p>Discription:[发放会员卡]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Map<String,Object>
     */
	public Map<String,Object> send(String accessToken, String userId,
                                   String cardNo, String point, String templateId, String balance, String vip, String appId);
	//删除会员卡
    public void deleteCard(Member member);
	/**
     * <p>Discription:[更改会员积分]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public String updatePoint(String targetCardNo, Date openDate, String point, String vip, String appId);
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

    AlipayMarketingCardQueryResponse getPassIdUrl(String aliCardNo, String aliUserId);


    AlipaySystemOauthTokenResponse flcxToken(String authCode, String appId);
    /** 人脸认证身份认证初始化服务（支付宝开放认证初始化服务）
      * @author sgmark@aliyun.com
      * @date 2019/8/30 0030
      * @param
      * @return
      */
    AlipayUserCertifyOpenInitializeResponse initializeAlipayUser (String certName, String certNo) throws AlipayApiException;

    AlipayUserCertifyOpenCertifyResponse certifyAlipayUser(String certifyId);

    AlipayUserCertifyOpenQueryResponse  certifyOpenQuery(String certifyId);
    /**
     * 刷脸支付初始化
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/26 0026
     * @Param:
     * @return:
     */
    ZolozAuthenticationCustomerSmilepayInitializeResponse smilePayInitialize(String apdidToken, String appName, String appVersion, String bioMetaInfo);
    /**
     *人脸ftoken查询消费接口(获取uId)
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/26 0026
     * @Param:
     * @return:
     */
    ZolozAuthenticationCustomerFtokenQueryResponse  customerFtokenQuery(String fToken);
}

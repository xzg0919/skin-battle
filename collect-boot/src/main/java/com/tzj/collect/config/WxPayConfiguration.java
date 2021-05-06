package com.tzj.collect.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Auther: xiangzhongguo
 * @Date: 2020-03-20 14:31
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WxPayConfiguration {
    private WxPayProperties properties;


    @Bean(autowire = Autowire.BY_NAME, value = "wxPayService")
    public WxPayService wxService() {
        return (WxPayService) getWxService(false, true);
    }


    @Bean(autowire = Autowire.BY_NAME, value = "wxAppPayService")
    public WxPayService wxAppService() {
        return (WxPayService) getWxService(true, true);
    }


    @Bean(autowire = Autowire.BY_NAME, value = "entpayService")
    public EntPayService entPayService() {
        return (EntPayService) getWxService(false, false);
    }


    @Bean(autowire = Autowire.BY_NAME, value = "appEntpayService")
    public EntPayService appEntPayService() {
        return (EntPayService) getWxService(true, false);
    }


/**
     *  微信service配置
     * @param isAppConfig
     * @param isWxPayService
     * @return
     */

    public Object getWxService(boolean isAppConfig, boolean isWxPayService) {
        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();

        //转账使用普通商户模式进行转账，收款使用服务商模式进行收款
        if(!isWxPayService){
            payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));
            payConfig.setMchKey(StringUtils.trimToNull(this.properties.getMchKey()));
            payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getKeyPath()));
            if (isAppConfig) {
                payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppIdApp()));
            } else {
                payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppIdWx()));
            }
        }else{
            if (isAppConfig) {
                payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppIdApp()));
            } else {
                payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppIdWx()));
            }
            payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));
            payConfig.setMchKey(StringUtils.trimToNull(this.properties.getMchKey()));
            payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getKeyPath()));
        }
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        wxPayService.setConfig(payConfig);
        if (isWxPayService) {
            return wxPayService;
        }
        return wxPayService.getEntPayService();
    }






}

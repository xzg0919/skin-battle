package com.tzj.iot.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.tzj.collect.common.constant.Const.ALI_APPID;
import static com.tzj.collect.common.constant.Const.ALI_PAY_KEY;


@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
@AllArgsConstructor
/**
 * @Auther: xiangzhongguo
 * @Date: 2021/5/17 11:38
 * @Description:
 */
@Slf4j
public class CertAliPayRequestConfig {

    private AliPayProperties properties;


    /**
     * 转账的支付宝client
     * @return
     */
    @Bean(autowire = Autowire.BY_NAME, value = "certAlipayClient")
    public AlipayClient CertAlipayClientInit() {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        //APPID 即创建应用后生成,详情见创建应用并获取 APPID
        certAlipayRequest.setAppId(ALI_APPID);
        //开发者应用私钥，由开发者自己生成
        certAlipayRequest.setPrivateKey(ALI_PAY_KEY);
        //参数返回格式，只支持 json 格式
        certAlipayRequest.setFormat("json");
        //请求和签名使用的字符编码格式，支持 GBK和 UTF-8
        certAlipayRequest.setCharset("UTF-8");
        //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
        certAlipayRequest.setSignType("RSA2");
        //应用公钥证书路径（app_cert_path 文件绝对路径）
        certAlipayRequest.setCertPath(this.properties.getAppCert());
        //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
        certAlipayRequest.setAlipayPublicCertPath(this.properties.getPublicCert());
        //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
        certAlipayRequest.setRootCertPath(this.properties.getRootCert());
        try {
           log.info("初始化证书Client成功");
            return new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            log.info("初始化证书Client失败");
            e.printStackTrace();
        }
        return null;
    }


}

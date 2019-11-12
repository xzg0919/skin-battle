package com.tzj.collect.controller;

import com.tzj.module.api.service.NoceService;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.ApiConfig;
import com.tzj.module.easyopen.ApiKeys;
import com.tzj.module.easyopen.support.ApiController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.*;


/**
 *
 * 以旧换新的api统一入口
 *
 * @Author michael_wang
 **/
@Controller
@RequestMapping(value = "/enterprise/api")
public class EnterpriseApiController extends ApiController{

    @Resource(name="apiNonceServiceImpl")
    private NoceService noceService;

    @Resource(name="enterpriseApiSubjectServiceImpl")
    private SubjectService subjectService;

    @Override
    protected void initApiConfig(ApiConfig apiConfig) {
        //设置开发者模式，即使api方法上面注明需要的，也不需要验证token以及签名
        apiConfig.setDevMode(false);

        //设置 appid 以及 签名key
        Map<String, String> appSecretStore = new HashMap<>();
        appSecretStore.put("app_id_5", "sign_key_99bbccdd");
        apiConfig.addAppSecret(appSecretStore);

        ApiKeys apiKeys=new ApiKeys();
        apiKeys.setTokenSignKey(false); //使用 appid的签名key
        apiKeys.setProduceSignKey(ENTERPRISE_API_TOKEN_SIGN_KEY);
        apiKeys.setTokenCyptoKey(ENTERPRISE_API_TOKEN_CYPTO_KEY);
        apiKeys.setTokenSecretKey(ENTERPRISE_API_TOKEN_SECRET_KEY);
        apiConfig.setApiKeys(apiKeys);

        //设置  SubjectService 以及 NoceService 的实现 =========
        apiConfig.setNoceService(noceService);
        apiConfig.setSubjectService(subjectService);

    }

}

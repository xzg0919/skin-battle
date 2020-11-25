package com.tzj.iot.controller;

import com.tzj.module.api.service.NoceService;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.ApiConfig;
import com.tzj.module.easyopen.ApiKeys;
import com.tzj.module.easyopen.support.ApiController;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.tzj.collect.common.constant.TokenConst.*;


/**
 * IOT API入口
 */
@Controller
@RequestMapping(value = "/iot4/api")
public class IOTApiController extends ApiController {

    @Resource(name = "apiNonceServiceImpl")
    private NoceService noceService;

    @Resource(name = "iotApiSubjectServiceImpl")
    private SubjectService subjectService;

    @Override
    protected void initApiConfig(ApiConfig apiConfig) {
        //设置开发者模式，即使api方法上面注明需要的，也不需要验证token以及签名
        apiConfig.setDevMode(false);

        //设置 appid 以及 签名key
        Map<String, String> appSecretStore = new HashMap<>();
        appSecretStore.put("app_id_2", "sign_key_55667788");
        apiConfig.addAppSecret(appSecretStore);

        ApiKeys apiKeys = new ApiKeys();
        apiKeys.setTokenSignKey(false); //使用 appid的签名key
        apiKeys.setProduceSignKey(EQUIPMENT_APP_API_TOKEN_SIGN_KEY);
        apiKeys.setTokenCyptoKey(EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);
        apiKeys.setTokenSecretKey(EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
        apiConfig.setApiKeys(apiKeys);

        //设置  SubjectService 以及 NoceService 的实现 =========
        apiConfig.setNoceService(noceService);
        apiConfig.setSubjectService(subjectService);
    }

}

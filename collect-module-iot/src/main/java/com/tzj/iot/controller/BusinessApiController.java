package com.tzj.iot.controller;

import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_CYPTO_KEY;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_SECRET_KEY;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_SIGN_KEY;
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

/**
 *
 * 企业端的api统一入口
 *
 * @Author michael_wang
 *
 */
@Controller
@RequestMapping(value = "/business/api")
public class BusinessApiController extends ApiController {

    @Resource(name = "apiNonceServiceImpl")
    private NoceService noceService;

    @Resource(name = "businessApiSubjectServiceImpl")
    private SubjectService subjectService;

    @Override
    protected void initApiConfig(ApiConfig apiConfig) {
        //设置开发者模式，即使api方法上面注明需要的，也不需要验证token以及签名
        apiConfig.setDevMode(false);

        //设置 appid 以及 签名key
        Map<String, String> appSecretStore = new HashMap<>();
        appSecretStore.put("app_id_3", "sign_key_99aabbcc");
        apiConfig.addAppSecret(appSecretStore);

        ApiKeys apiKeys = new ApiKeys();
        //动态签名
        apiKeys.setTokenSignKey(true); //使用 appid的签名key
        apiKeys.setProduceSignKey(BUSINESS_API_TOKEN_SIGN_KEY);
        apiKeys.setTokenCyptoKey(BUSINESS_API_TOKEN_CYPTO_KEY);
        apiKeys.setTokenSecretKey(BUSINESS_API_TOKEN_SECRET_KEY);
        apiConfig.setApiKeys(apiKeys);

        //设置  SubjectService 以及 NoceService 的实现 =========
        apiConfig.setNoceService(noceService);
        apiConfig.setSubjectService(subjectService);

    }

}

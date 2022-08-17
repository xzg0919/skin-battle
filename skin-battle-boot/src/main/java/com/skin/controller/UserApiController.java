package com.skin.controller;

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

import static com.skin.common.constant.TokenConst.*;


/**
*
* ADMIN统一登录入口
*
* @Author michael_wang
**/
@Controller
@RequestMapping(value = "/user/api")
public class UserApiController extends ApiController{

	 @Resource(name="apiNonceServiceImpl")
	    private NoceService noceService;

	    @Resource(name="userApiSubjectServiceImpl")
	    private SubjectService subjectService;

	    @Override
	    protected void initApiConfig(ApiConfig apiConfig) {
	        //设置开发者模式，即使api方法上面注明需要的，也不需要验证token以及签名
	        apiConfig.setDevMode(false);

	        //设置 appid 以及 签名key
	        Map<String, String> appSecretStore = new HashMap<>();
	        appSecretStore.put("app_id_1", "sign_key_778899");
	        apiConfig.addAppSecret(appSecretStore);

	        ApiKeys apiKeys=new ApiKeys();
	        apiKeys.setTokenSignKey(false); //使用 appid的签名key
	        apiKeys.setProduceSignKey(USER_API_TOKEN_SIGN_KEY);
	        apiKeys.setTokenCyptoKey(USER_API_TOKEN_CYPTO_KEY);
	        apiKeys.setTokenSecretKey(USER_API_TOKEN_SECRET_KEY);
	        apiConfig.setApiKeys(apiKeys);

	        //设置  SubjectService 以及 NoceService 的实现 =========
	        apiConfig.setNoceService(noceService);
	        apiConfig.setSubjectService(subjectService);

	    }
	
}

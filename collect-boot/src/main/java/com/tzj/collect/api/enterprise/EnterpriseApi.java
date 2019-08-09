package com.tzj.collect.api.enterprise;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.enterprise.EnterpriseBean;
import com.tzj.collect.core.service.EnterpriseAccountService;
import com.tzj.collect.core.service.EnterpriseService;
import com.tzj.collect.entity.Enterprise;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.api.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

@ApiService
public class EnterpriseApi {
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseAccountService enterpriseAccountService;

    /**
     * 以旧换新企业登录
     * @param
     * @return
     */
    @Api(name="enterprise.login",version="1.0")
    @SignIgnore
    @AuthIgnore
    public Object login(EnterpriseBean enterpriseBean)throws ApiException{

        if(StringUtils.isBlank(enterpriseBean.getUserName())||StringUtils.isBlank(enterpriseBean.getPassword())){
            throw new ApiException("用户名或密码不能为空");
        }
        EnterpriseAccount enterpriseAccount = enterpriseAccountService.selectOne(new EntityWrapper<EnterpriseAccount>().eq("user_name", enterpriseBean.getUserName()).eq("password_", enterpriseBean.getPassword()).eq("del_flag",0));

        if(null==enterpriseAccount){
            throw new ApiException("用户名或密码不正确");
        }else{
            Map<String,Object> map = new HashMap<String,Object>();
            Enterprise enterprise = enterpriseService.selectById(enterpriseAccount.getEnterpriseId());
            String token = JwtUtils.generateToken(enterpriseAccount.getEnterpriseId().toString(),ENTERPRISE_API_EXPRIRE, ENTERPRISE_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, ENTERPRISE_API_TOKEN_CYPTO_KEY);
            System.out.println("生成的依旧换新企业token是："+securityToken);
            map.put("enterprise",enterprise);
            map.put("token",securityToken);
            return map;
        }
    }
}

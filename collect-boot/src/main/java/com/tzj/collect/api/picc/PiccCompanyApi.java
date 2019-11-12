package com.tzj.collect.api.picc;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.picc.PiccCompanyBean;
import com.tzj.collect.core.service.PiccCompanyService;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.api.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.*;

@ApiService
public class PiccCompanyApi {

    @Autowired
    private PiccCompanyService piccCompanyService;

    /**
     * picc企业登录接口
     * @param
     * @return
     */
    @Api(name = "picc.piccLogin", version = "1.0")
    @SignIgnore
    @AuthIgnore
    @DS("slave")
    public Object piccLogin(PiccCompanyBean piccCompanyBean){
        PiccCompany piccCompany = piccCompanyService.selectOne(new EntityWrapper<PiccCompany>().eq("user_name", piccCompanyBean.getUserName()).eq("password", piccCompanyBean.getPassword()).eq("del_flag", 0));
        if(null==piccCompany){
            return "用户名或密码不正确";
        }
        String token = JwtUtils.generateToken(piccCompany.getId().toString(),PICC_API_EXPRIRE, PICC_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, PICC_API_TOKEN_CYPTO_KEY);
        System.out.println("生成picc的token是 ："+securityToken);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",securityToken);
        map.put("piccCompany",piccCompany);
        return map;
    }
}

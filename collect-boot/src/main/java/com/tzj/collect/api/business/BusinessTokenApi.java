package com.tzj.collect.api.business;

import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.service.CompanyAccountService;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tzj.collect.common.constant.TokenConst.*;

@ApiService
public class BusinessTokenApi {
	@Autowired
	private CompanyAccountService companyAccountService;

    /**
     * 回收企业获取token
     * 忽略token验证，需要sign签名验证
     */
    @Api(name = "business.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public TokenBean getToken(CompanyAccountBean companyAccountBean) {

       //todo
       //判断用户名和密码
    	CompanyAccount companyAccount = companyAccountService.selectByUsername(companyAccountBean.getUsername(),companyAccountBean.getPassword());
        if(companyAccount!=null){
            String token = JwtUtils.generateToken(companyAccount.getId().toString(), BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);
            TokenBean tokenBean = new TokenBean();
            tokenBean.setExpire(BUSINESS_API_EXPRIRE);
            tokenBean.setSignKey(SignUtils.produceSignKey(token, BUSINESS_API_TOKEN_SIGN_KEY));
            tokenBean.setToken(securityToken);
            return tokenBean;
        }else{
        	  throw new ApiException("用户名或者密码错误!");
        }
    }

    /**
     * 刷新token
     * 需要token验证，忽略sign签名验证
     * 需要 BUSINESS_API_COMMON_AUTHORITY 权限
     *
     * @return
     */
    @Api(name = "business.token.flush", version = "1.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public TokenBean flushToken() {
    	Subject subject=ApiContext.getSubject();
        //接口里面获取  CompanyAccount 的例子
        CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();

        String token = JwtUtils.generateToken(subject.getId(), BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(BUSINESS_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;

    }
}

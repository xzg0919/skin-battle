package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyAccountMapper;
import com.tzj.collect.core.param.business.CompanyAccountBean;
import com.tzj.collect.core.service.CompanyAccountService;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.core.param.token.TokenBean;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_EXPRIRE;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_CYPTO_KEY;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_SECRET_KEY;
import static com.tzj.common.constant.TokenConst.BUSINESS_API_TOKEN_SIGN_KEY;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.easyopen.exception.ApiException;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyAccountServiceImpl extends ServiceImpl<CompanyAccountMapper, CompanyAccount> implements CompanyAccountService {

    /**
     * 判断用户名和密码
     */
    @Override
    public CompanyAccount selectByUsername(String userName, String passWord) {
        EntityWrapper<CompanyAccount> wrapper = new EntityWrapper<CompanyAccount>();
        wrapper.eq("username", userName);
        wrapper.eq("password_", passWord);
        return this.selectOne(wrapper);
    }

    @Override
    public TokenBean login(CompanyAccountBean accountBean) {
        EntityWrapper<CompanyAccount> wrapper = new EntityWrapper<CompanyAccount>();
        wrapper.eq("username", accountBean.getUsername());
        wrapper.eq("password_", accountBean.getPassword());
        CompanyAccount companyAccount = this.selectOne(wrapper);
        if (companyAccount != null) {
            companyAccount.setStatus_("1");
            companyAccount.setUpdateDate(new Date());
            if (this.insertOrUpdate(companyAccount)) {
                String token = JwtUtils.generateToken(companyAccount.getId().toString(), BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(BUSINESS_API_EXPRIRE);
                tokenBean.setSignKey(SignUtils.produceSignKey(token, BUSINESS_API_TOKEN_SIGN_KEY));
                tokenBean.setToken(securityToken);
                return tokenBean;
            }
        } else {
            throw new ApiException("用户名或密码错误");
        }
        return null;
    }

}

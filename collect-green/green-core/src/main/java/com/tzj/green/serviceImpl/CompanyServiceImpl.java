package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.Company;
import com.tzj.green.mapper.CompanyMapper;
import com.tzj.green.param.CompanyBean;
import com.tzj.green.service.CompanyService;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.green.common.content.TokenConst.*;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收企业表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService
{
    @Resource
    private CompanyMapper CompanyMapper;

    @Override
    public Object getToken(CompanyBean companyBean) {
        Company company = this.selectOne(new EntityWrapper<Company>().eq("tel", companyBean.getTel()).eq("password_", companyBean.getPassword()).eq("del_flag", "0"));
        if (null == company){
            throw new ApiException("用户名或密码错误");
        }
        String token = JwtUtils.generateToken(company.getId().toString(), BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("company",company);
        resultMap.put("token",securityToken);
        return resultMap;
    }
}
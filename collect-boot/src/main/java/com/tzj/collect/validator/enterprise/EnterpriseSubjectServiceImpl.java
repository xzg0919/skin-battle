package com.tzj.collect.validator.enterprise;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.service.EnterpriseAccountService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.ENTERPRISE_API_COMMON_AUTHORITY;

@Service("enterpriseApiSubjectServiceImpl")
public class EnterpriseSubjectServiceImpl implements SubjectService {

    private final String cacheName="enterpriseCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EnterpriseAccountService enterpriseAccountService;


    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }
        EnterpriseAccount enterpriseAccount = enterpriseAccountService.selectOne(new EntityWrapper<EnterpriseAccount>().eq("enterprise_id",subjectKey).eq("del_flag",0));

        Subject subject=new Subject();
        subject.setId(enterpriseAccount.getId().toString());
        subject.setName(enterpriseAccount.getUserName());
        subject.setUser(enterpriseAccount);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ENTERPRISE_API_COMMON_AUTHORITY);
        subject.setAuthorities(authorities);

        setSubjectCache(subject);

        return subject;
    }
    @Override
    public void setSubjectCache(Subject subject) {
        CacheUtils.setCache(subject,cacheManager,cacheName);
    }

    @Override
    public Subject getSubjectCache(String subjectKey) {
        return CacheUtils.getSubjectByCache(cacheManager,cacheName,subjectKey);
    }

    @Override
    public void removeSubjectCache(Subject subject) {
        CacheUtils.removeCache(subject,cacheManager,cacheName);
    }
}

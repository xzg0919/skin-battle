package com.tzj.collect.validator.business;

import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.service.CompanyAccountService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("businessApiSubjectServiceImpl")
public class BusinessSubjectServiceImpl implements SubjectService{

    private final String cacheName="companyCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CompanyAccountService companyAccountService;

    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }
        
        CompanyAccount companyAccount=companyAccountService.selectById(Long.parseLong(subjectKey));

        Subject subject=new Subject();
        subject.setId(companyAccount.getId().toString());
        subject.setName(companyAccount.getUsername());
        subject.setUser(companyAccount);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(BUSINESS_API_COMMON_AUTHORITY);
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

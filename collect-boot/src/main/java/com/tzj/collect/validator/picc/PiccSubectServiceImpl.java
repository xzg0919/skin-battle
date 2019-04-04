package com.tzj.collect.validator.picc;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.service.PiccCompanyService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.tzj.collect.common.constant.TokenConst.*;

@Service("piccSubectServiceImpl")
public class PiccSubectServiceImpl implements SubjectService {

    private final String cacheName="piccCache";

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private PiccCompanyService piccCompanyService;

    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }
        PiccCompany piccCompany = piccCompanyService.selectById(subjectKey);

        Subject subject=new Subject();
        subject.setId(piccCompany.getId().toString());
        subject.setName(piccCompany.getUserName());
        subject.setUser(piccCompany);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(PICC_API_COMMON_AUTHORITY);
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

package com.tzj.collect.validator.picc;

import com.tzj.collect.core.service.PiccCompanyService;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.PICC_API_COMMON_AUTHORITY;

@Service("piccSubectServiceImpl")
public class PiccSubectServiceImpl implements SubjectService {

    private final String cacheName="piccCache";

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private PiccCompanyService piccCompanyService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        PiccCompany piccCompany = piccCompanyService.selectById(key);

        Subject subject=new Subject();
        subject.setId(piccCompany.getId().toString());
        subject.setName(piccCompany.getUserName());
        subject.setUser(piccCompany);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(PICC_API_COMMON_AUTHORITY);
        subject.setAuthorities(authorities);

        setSubjectCache(token,subject);

        return subject;
    }


    @Override
    public void setSubjectCache(String token,Subject subject) {
        EhCache2Utils.setCache(token,subject,cacheManager,cacheName);
    }

    @Override
    public Subject getSubjectCache(String token) {
        return EhCache2Utils.getSubjectByCache(cacheManager,cacheName,token);
    }


    @Override
    public void removeSubjectCache(String token) {
        EhCache2Utils.removeCache(token,cacheManager,cacheName);
    }
}

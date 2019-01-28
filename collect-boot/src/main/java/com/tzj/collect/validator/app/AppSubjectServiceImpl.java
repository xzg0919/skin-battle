package com.tzj.collect.validator.app;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

import java.util.LinkedList;

import com.tzj.collect.common.util.CacheUtils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.RecyclersService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("appApiSubjectServiceImpl")
public class AppSubjectServiceImpl implements SubjectService {

    private final String cacheName="recyclersCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RecyclersService recyclersService;

    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {

        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }

        Recyclers recyclers=recyclersService.selectById(Long.parseLong(subjectKey));

        Subject subject=new Subject();
        subject.setId(recyclers.getId().toString());
        subject.setName(recyclers.getName());
        subject.setUser(recyclers);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(APP_API_COMMON_AUTHORITY);
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

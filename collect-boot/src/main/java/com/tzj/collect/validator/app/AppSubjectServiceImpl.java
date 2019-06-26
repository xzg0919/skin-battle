package com.tzj.collect.validator.app;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

import java.util.LinkedList;
import java.util.Map;

import com.tzj.module.easyopen.util.EhCache2Utils;
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
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {

        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        Recyclers recyclers=recyclersService.selectById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(recyclers.getId().toString());
        subject.setName(recyclers.getName());
        subject.setUser(recyclers);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(APP_API_COMMON_AUTHORITY);
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

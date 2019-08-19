package com.tzj.point.validator.appAdmin;

import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import com.tzj.point.entity.JfappRecycler;
import com.tzj.point.service.JfappRecyclerService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.point.common.constant.TokenConst.JFAPP_API_COMMON_AUTHORITY;


/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("appApiSubjectServiceImpl")
public class AppSubjectServiceImpl implements SubjectService {

    private final String cacheName="recyclersCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JfappRecyclerService jfappRecyclerService;

    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {

        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        JfappRecycler jfappRecycler = jfappRecyclerService.selectById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(jfappRecycler.getId().toString());
        subject.setName(jfappRecycler.getName());
        subject.setUser(jfappRecycler);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(JFAPP_API_COMMON_AUTHORITY);
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

package com.tzj.green.terminal;

import com.tzj.green.entity.Area;
import com.tzj.green.service.AreaService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.green.common.content.TokenConst.*;


/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("businessApiSubjectServiceImpl")
public class BusinessSubjectServiceImpl implements SubjectService{

    private final String cacheName="companyCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AreaService areaService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        
        Area area = areaService.selectById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(area.getId().toString());
        subject.setName(area.getAreaName());
        subject.setUser(area);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(BUSINESS_API_COMMON_AUTHORITY);
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

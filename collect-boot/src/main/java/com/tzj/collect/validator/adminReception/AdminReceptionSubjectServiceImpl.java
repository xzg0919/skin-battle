package com.tzj.collect.validator.adminReception;

import com.tzj.collect.core.service.AdminReceptionService;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.AdminReception;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.*;

/**
 * 
 * @ClassName: adminSubjectServiceImpl 
 * @Description: TODO
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:25:14
 */

@Service("adminReceptionApiSubjectServiceImpl")
public class AdminReceptionSubjectServiceImpl implements SubjectService{

    private final String cacheName="adminReceptionCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AdminReceptionService adminReceptionService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        AdminReception adminReception = adminReceptionService.selectById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(adminReception.getId().toString());
        subject.setName(adminReception.getUsername());
        subject.setUser(adminReception);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ADMIN_RECEPTION_API_COMMON_AUTHORITY);
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

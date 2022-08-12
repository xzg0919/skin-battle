package com.skin.validator.admin;

import com.skin.core.service.AdminService;
import com.skin.entity.Admin;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;


/**
 * 
 * @ClassName: adminSubjectServiceImpl 
 * @Description: TODO
 * @date: 2018年3月16日 上午11:25:14
 */

@Service("adminApiSubjectServiceImpl")
public class AdminSubjectServiceImpl implements SubjectService{

    private final String cacheName="adminCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AdminService adminService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        
        Admin admin=adminService.getById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(admin.getId().toString());
        subject.setName(admin.getUserName());
        subject.setUser(admin);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ADMIN_API_COMMON_AUTHORITY);
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

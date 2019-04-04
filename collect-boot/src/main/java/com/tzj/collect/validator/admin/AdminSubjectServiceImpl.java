package com.tzj.collect.validator.admin;

import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

import java.util.LinkedList;

import com.tzj.collect.common.util.CacheUtils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tzj.collect.entity.Admin;
import com.tzj.collect.service.AdminService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;

/**
 * 
 * @ClassName: adminSubjectServiceImpl 
 * @Description: TODO
 * @author: 向忠国
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
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }
        
        Admin admin=adminService.selectById(Long.parseLong(subjectKey));

        Subject subject=new Subject();
        subject.setId(admin.getId().toString());
        subject.setName(admin.getUsername());
        subject.setUser(admin);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ADMIN_API_COMMON_AUTHORITY);
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

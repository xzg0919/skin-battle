package com.skin.validator.user;

import com.skin.core.service.AdminService;
import com.skin.core.service.UserService;
import com.skin.entity.Admin;
import com.skin.entity.User;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;




@Service("userApiSubjectServiceImpl")
public class UserSubjectServiceImpl implements SubjectService{

    private final String cacheName="userCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        
        User user=userService.getById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(user.getId().toString());
        subject.setName(user.getNickName());
        subject.setUser(user);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(USER_API_COMMON_AUTHORITY);
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

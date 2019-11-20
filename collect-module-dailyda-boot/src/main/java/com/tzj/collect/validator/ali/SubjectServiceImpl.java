package com.tzj.collect.validator.ali;

import com.tzj.collect.entity.Member;
import com.tzj.collect.core.service.DailyMemberService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;


/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("aliApiSubjectServiceImpl")
public class SubjectServiceImpl implements SubjectService{

    private final String cacheName="memberCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DailyMemberService dailyMemberService;

    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        Member member = dailyMemberService.selectMemberByAliUserId(key);

        Subject subject=new Subject();
        subject.setId(member.getId().toString());
        subject.setName(member.getName());
        subject.setUser(member);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ALI_API_COMMON_AUTHORITY);
        subject.setAuthorities(authorities);

        //TODO： 分表后，后面需要取消 User 对象


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

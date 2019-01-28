package com.tzj.collect.validator.ali;

import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.MemberService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

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
    private MemberService memberService;

    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }

        Member member=memberService.selectById(Long.parseLong(subjectKey));

        Subject subject=new Subject();
        subject.setId(member.getId().toString());
        subject.setName(member.getName());
        subject.setUser(member);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ALI_API_COMMON_AUTHORITY);
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

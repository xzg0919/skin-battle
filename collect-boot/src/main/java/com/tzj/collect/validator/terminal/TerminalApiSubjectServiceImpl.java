package com.tzj.collect.validator.terminal;

import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.collect.service.EnterpriseTerminalService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

import static com.tzj.collect.common.constant.TokenConst.*;


@Service("terminalApiSubjectServiceImpl")
public class TerminalApiSubjectServiceImpl implements SubjectService {

    private final String cacheName="terminalCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EnterpriseTerminalService enterpriseTerminalService;


    @Override
    public Subject getSubjectByTokenSubject(String subjectKey) {
        Subject subjectCache=getSubjectCache(subjectKey);
        if(subjectCache!=null){
            return subjectCache;
        }
        EnterpriseTerminal enterpriseTerminal = enterpriseTerminalService.selectById(subjectKey);

        Subject subject=new Subject();
        subject.setId(enterpriseTerminal.getId().toString());
        subject.setName(enterpriseTerminal.getName());
        subject.setUser(enterpriseTerminal);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(TERMINAL_API_COMMON_AUTHORITY);
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

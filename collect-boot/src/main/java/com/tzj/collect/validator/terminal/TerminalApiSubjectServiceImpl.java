package com.tzj.collect.validator.terminal;

import com.tzj.collect.common.util.CacheUtils;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.collect.service.EnterpriseTerminalService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;


@Service("terminalApiSubjectServiceImpl")
public class TerminalApiSubjectServiceImpl implements SubjectService {

    private final String cacheName="terminalCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EnterpriseTerminalService enterpriseTerminalService;



    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }
        EnterpriseTerminal enterpriseTerminal = enterpriseTerminalService.selectById(key);

        Subject subject=new Subject();
        subject.setId(enterpriseTerminal.getId().toString());
        subject.setName(enterpriseTerminal.getName());
        subject.setUser(enterpriseTerminal);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(TERMINAL_API_COMMON_AUTHORITY);
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

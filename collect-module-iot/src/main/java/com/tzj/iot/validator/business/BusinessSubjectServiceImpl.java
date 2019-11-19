package com.tzj.iot.validator.business;

import com.tzj.collect.core.service.CompanyAccountService;
import com.tzj.collect.entity.CompanyAccount;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import java.util.LinkedList;
import java.util.Map;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 胡方明（12795880@qq.com）
 *
 */
@Service("businessApiSubjectServiceImpl")
public class BusinessSubjectServiceImpl implements SubjectService {

    private final String cacheName = "companyCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CompanyAccountService companyAccountService;

    @Override
    public Subject getSubjectByTokenSubject(String token, String key, Map<String, Object> map) {
        Subject subjectCache = getSubjectCache(token);
        if (subjectCache != null) {
            return subjectCache;
        }

        CompanyAccount companyAccount = companyAccountService.selectById(Long.parseLong(key));

        Subject subject = new Subject();
        subject.setId(companyAccount.getId().toString());
        subject.setName(companyAccount.getUsername());
        subject.setUser(companyAccount);

        LinkedList<String> authorities = new LinkedList<>();
        authorities.add(BUSINESS_API_COMMON_AUTHORITY);
        subject.setAuthorities(authorities);

        setSubjectCache(token, subject);

        return subject;
    }

    @Override
    public void setSubjectCache(String token, Subject subject) {
        EhCache2Utils.setCache(token, subject, cacheManager, cacheName);
    }

    @Override
    public Subject getSubjectCache(String token) {
        return EhCache2Utils.getSubjectByCache(cacheManager, cacheName, token);
    }

    @Override
    public void removeSubjectCache(String token) {
        EhCache2Utils.removeCache(token, cacheManager, cacheName);
    }

}

package com.tzj.collect.validator.enterprise;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.service.EnterpriseAccountService;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ENTERPRISE_API_COMMON_AUTHORITY;

@Service("enterpriseApiSubjectServiceImpl")
public class EnterpriseSubjectServiceImpl implements SubjectService {

    private final String cacheName="enterpriseCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EnterpriseAccountService enterpriseAccountService;


    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {

        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        EnterpriseAccount enterpriseAccount = enterpriseAccountService.selectOne(new EntityWrapper<EnterpriseAccount>().eq("enterprise_id",key).eq("del_flag",0));


        Subject subject=new Subject();
        subject.setId(enterpriseAccount.getId().toString());
        subject.setName(enterpriseAccount.getUserName());
        subject.setUser(enterpriseAccount);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(ENTERPRISE_API_COMMON_AUTHORITY);
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

package com.tzj.collect.flcx.validator.ali;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.service.FlcxMerchantService;
import com.tzj.collect.entity.FlcxMerchant;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.FLCX_API_COMMON_AUTHORITY;


/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("searchApiSubjectServiceImpl")
public class ThirdSubjectServiceImpl implements SubjectService{

    private final String cacheName="merchantCache";

    @Autowired
    private CacheManager cacheManager;
    @Reference(version = "${flcx.service.version}")
    private FlcxMerchantService flcxMerchantService;
    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {
        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        Subject subject=new Subject();

        FlcxMerchant flcxMerchant = flcxMerchantService.selectOne(new EntityWrapper<FlcxMerchant>().eq("del_flag", 0).eq("app_id", key));
        subject.setId(flcxMerchant.getId().toString());
        subject.setName(flcxMerchant.getAppId());
        subject.setUser(flcxMerchant);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(FLCX_API_COMMON_AUTHORITY);
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

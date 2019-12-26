package com.tzj.iot.validator.app;

import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.RecyclersService;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.service.SubjectService;
import com.tzj.module.easyopen.util.EhCache2Utils;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.EQUIPMENT_APP_API_COMMON_AUTHORITY;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("equipmentAppApiSubjectServiceImpl")
public class EquipmentAppSubjectServiceImpl implements SubjectService {

    private final String cacheName="equipmentAppCache";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CompanyEquipmentService companyEquipmentService;

    @Override
    public Subject getSubjectByTokenSubject(String token,String key, Map<String, Object> map) {

        Subject subjectCache=getSubjectCache(token);
        if(subjectCache!=null){
            return subjectCache;
        }

        CompanyEquipment companyEquipment = companyEquipmentService.selectById(Long.parseLong(key));

        Subject subject=new Subject();
        subject.setId(companyEquipment.getId().toString());
        subject.setName(companyEquipment.getEquipmentCode());
        subject.setUser(companyEquipment);

        LinkedList<String> authorities=new LinkedList<>();
        authorities.add(EQUIPMENT_APP_API_COMMON_AUTHORITY);
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

package com.tzj.collect.common.util;

import com.tzj.module.api.entity.Subject;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * cache 工具类
 * @Author 胡方明（12795880@qq.com）
 *
 **/
public class CacheUtils {
    public static Cache getCache(CacheManager cacheManager,String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null){
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    public static Subject getSubjectByCache(CacheManager cacheManager,String cacheName,String subjectKey){
        if(StringUtils.isBlank(subjectKey)){
            return null;
        }

        Element element = getCache(cacheManager,cacheName).get(subjectKey);
        return element==null?null: (Subject) element.getObjectValue();
    }

    public static void removeCache(Subject subject,CacheManager cacheManager,String cacheName){
        if(subject==null){
            return;
        }
        getCache(cacheManager,cacheName).remove(subject.getId());
    }

    public static void setCache(Subject subject,CacheManager cacheManager,String cacheName){
        if(subject==null){
            return;
        }

        if(getSubjectByCache(cacheManager,cacheName,subject.getId())!=null){
            removeCache(subject,cacheManager,cacheName);
        }

        Element element = new Element(subject.getId(), subject);
        getCache(cacheManager,cacheName).put(element);
    }
}

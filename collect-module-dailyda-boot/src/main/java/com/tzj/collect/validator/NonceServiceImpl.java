package com.tzj.collect.validator;

import com.tzj.module.api.service.NoceService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service("apiNonceServiceImpl")
public class NonceServiceImpl implements NoceService{

    private final String cacheName="noceCache";

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void addNoceCache(String noce) {
        Element element = new Element(noce, noce);
        getCache(cacheName).put(element);
    }

    @Override
    public boolean existsNoceCache(String noce) {
        Element element = getCache(cacheName).get(noce);
        if(element==null || element.getObjectValue()==null){
            return false;
        }
        return true;
    }

    private Cache getCache(String cacheName){
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null){
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }
}

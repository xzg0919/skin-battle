package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.DailyLexiconMapper;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.service.DailyLexiconService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author sgmark
 * @create 2019-08-09 16:08
 **/
@Service
@Transactional
public class DailyLexiconServiceImpl extends ServiceImpl<DailyLexiconMapper, DailyLexicon> implements DailyLexiconService {

    @Resource
    private  DailyLexiconMapper dailyLexiconMapper;
    /**  各个随机取2值
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Override
    public Set<Map<String, Object>> dailyLexiconList() {
        Set<Map<String, Object>> returnList = new HashSet<>();
        List<Map<String, Object>> mapList = lexiconList("lexiconList");
        //设置开始时间
        Long startTime = System.currentTimeMillis();
        while (returnList.size() < 10 && System.currentTimeMillis() - startTime <= 200) {
            mapList.stream().forEach(mapLists -> {
                int size = returnList.size();
                if (size > 10) {
                    return;
                }
                List<Object> depthLists = (ArrayList) mapLists.get("depthList");
                //未考虑list大小只为1的情况(会出现死循环)
                if (depthLists.size() == 1) {
                    returnList.add((Map<String, Object>) depthLists.get(0));
                } else {
                    while (returnList.size() - size < 2 && returnList.size() != 10) {
                        returnList.add((Map<String, Object>) depthLists.get(new Random().nextInt(depthLists.size())));
                    }
                }
            });
        }
        //去除答案
        returnList.stream().forEach(returnLists ->returnLists.remove("type_id"));
        return returnList;
    }


    /**   总的list
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Override
    @Cacheable(value = "dailyLexiconList", key = "#lexiconList", sync = true)
    public List<Map<String, Object>> lexiconList(String lexiconList) {
        return dailyLexiconMapper.lexiconList();
    }
}


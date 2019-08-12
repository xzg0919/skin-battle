package com.tzj.collect.api;

import com.tzj.collect.service.DailyLexiconService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 每日答答答api
 *
 * @author sgmark
 * @create 2019-08-07 15:07
 **/
@ApiService
public class DailyDaApi {


    @Resource
    private DailyLexiconService dailyLexiconService;

    /** 各取值2题
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Api(name = "lex.check", version = "1.0", ignoreTimestamp = true, ignoreNonce = true)
    @AuthIgnore
    @SignIgnore
    public Set<Map<String, Object>> dailyLexiconList(){
        return dailyLexiconService.dailyLexiconList();
    }
}

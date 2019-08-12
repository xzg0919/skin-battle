package com.tzj.collect.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.entity.DailyLexicon;
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

  /** 根据用户授权返回的authCode,解析用户的数据
    * @author sgmark@aliyun.com
    * @date 2019/8/12 0012
    * @param
    * @return
    */
    @Api(name = "member.getAuthCode", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getAuthCode(Map map) {
        return null;
    }

    /** (名称头像，本周得分，排名)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Map<String, Object> memberInfo(){
        return null;
    }

    /** 本周达人榜(头像，得分，名次), 需要分页（包含top50的数据， page最大值取到(假设每页10条，最大页数取值为5)）
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Page<Map<String, Object>> weekDresserList(){
        return null;
    }
    /** 总环保荣誉榜(头像，得分，城市，多少周)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Page<Map<String, Object>> eachWeekDresserList(){
        return null;
    }


    /** 各取值2题
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Api(name = "daily.list", version = "1.0", ignoreTimestamp = true, ignoreNonce = true)
    @AuthIgnore
    @SignIgnore
    public Set<Map<String, Object>> dailyLexiconList(){
        return dailyLexiconService.dailyLexiconList();
    }

    /** 答题验证
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param dailyLexicon (参数为题目id及提交答案类型(英文字母))
      * @return
      */
    public Map<String, Object> lexiconChecking(DailyLexicon dailyLexicon){
        return null;
    }

    /** 本周累计分数，今日答题得分，周排名，今日答题正确错误数据
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Map<String, Object> weekRecords(){
        return null;
    }

    /**  错题记录(包含未答题)，今日答题正确数，错误数
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Map<String, Object> errorLexiconList(){
        return null;
    }

    //---------------------------------------------帅气的分割线----------------------------------------------

    /** 用户领取红包记录(包含本周答题记录)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Map<String, Object> memberReceivingRecords(){
        return null;
    }

    /** 用户领取现金红包(领取前判断本周领取次数)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    public Map<String, Object> receivingMoney(){
        return null;
    }
}

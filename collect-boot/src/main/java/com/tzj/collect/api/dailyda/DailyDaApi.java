package com.tzj.collect.api.dailyda;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.api.lexicon.param.DailyDaParam;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.DailyLexicon;
import com.tzj.collect.entity.DailyWeekRanking;
import com.tzj.collect.service.DailyLexiconService;
import com.tzj.collect.service.DailyReceivingService;
import com.tzj.collect.service.DailyWeekRankingService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private DailyReceivingService dailyReceivingService;

    @Resource
    private DailyWeekRankingService dailyWeekRankingService;

//  /** 根据用户授权返回的authCode,解析用户的数据
//    * @author sgmark@aliyun.com
//    * @date 2019/8/12 0012
//    * @param
//    * @return
//    */
//    @Api(name = "d.getAuthCode", version = "1.0")
//    @SignIgnore
//    @AuthIgnore
//    public Object getAuthCode(Map map) {
//        return null;
//    }

    /** (名称头像，本周得分，排名)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.member.info", version = "1.0")
    public Map<String, Object> memberInfo(){
        return dailyLexiconService.memberInfo(MemberUtils.getMember());
    }

    /** 本周达人榜(头像，得分，名次), 需要分页（包含top50的数据， page最大值取到(假设每页10条，最大页数取值为5)）
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.week.dresser", version = "1.0")
    public List<Map<String, Object>> weekDresserList(PageBean pageBean){
        Integer startPage = null == pageBean.getPageNumber() ? 1: pageBean.getPageNumber();
        Integer pageSize = null == pageBean.getPageSize() ? 10 : pageBean.getPageSize();
        if (startPage * pageSize > 50){
            startPage = 50/pageSize;
        }
        return dailyLexiconService.weekDresserList(startPage.intValue(), pageSize);
    }
    /** 总环保荣誉榜(头像，得分，城市，多少周)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.all.week.dresser", version = "1.0")
    public Page<DailyWeekRanking> eachWeekDresserList(PageBean pageBean){
        return dailyWeekRankingService.eachWeekDresserList(pageBean.getPageNumber(), pageBean.getPageSize());
    }


    /** 各取值2题
     * @author sgmark@aliyun.com
     * @date 2019/8/9 0009
     * @param
     * @return
     */
    @Api(name = "daily.list", version = "1.0")
    public Set<Map<String, Object>> dailyLexiconList(){
        if (dailyLexiconService.isAnswerDaily(MemberUtils.getMember().getAliUserId()).size()>0){
           throw new ApiException("今日答题已完成，明儿请赶早");
        }
        return dailyLexiconService.dailyLexiconList(MemberUtils.getMember().getAliUserId());
    }

    /** 答题验证
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param dailyDaParam (参数为题目id及提交答案类型(英文字母))
      * @return
      */
    @Api(name = "daily.check", version = "1.0")
    public Map<String, Object> lexiconChecking(DailyDaParam dailyDaParam){
        dailyDaParam.setAliUserId(MemberUtils.getMember().getAliUserId());
        return dailyLexiconService.lexiconChecking(dailyDaParam);
    }

    /** 本周累计分数，今日答题得分，周排名，今日答题正确错误数据
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.week.records", version = "1.0")
    public Map<String, Object> weekRecords(){
        return dailyLexiconService.weekRecords(MemberUtils.getMember());
    }

    /**  错题记录(包含未答题)，今日答题正确数，错误数
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.error.list", version = "1.0")
    public Map<String, Object> errorLexiconList(){
        return dailyLexiconService.errorLexiconList(MemberUtils.getMember());
    }

    //---------------------------------------------帅气的分割线----------------------------------------------

    /** 用户领取红包记录(包含本周答题记录)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.member.receiving", version = "1.0")
    public Map<String, Object> memberReceivingRecords(){
        return dailyReceivingService.memberReceivingRecords(MemberUtils.getMember().getAliUserId());
    }

    /** 用户领取现金红包(领取前判断本周领取次数)
      * @author sgmark@aliyun.com
      * @date 2019/8/12 0012
      * @param
      * @return
      */
    @Api(name = "daily.receiving.money", version = "1.0")
    public Map<String, Object> receivingMoney(DailyDaParam dailyDaParam){
        dailyDaParam.setAliUserId(MemberUtils.getMember().getAliUserId());
        return dailyReceivingService.receivingMoney(dailyDaParam);
    }
}

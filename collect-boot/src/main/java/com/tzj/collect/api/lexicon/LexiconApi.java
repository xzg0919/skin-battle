package com.tzj.collect.api.lexicon;

import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 关键字搜索
 *
 * @author sgmark
 * @create 2019-06-17 16:12
 **/
@ApiService
public class LexiconApi {

    @Resource
    private FlcxLexiconService flcxLexiconService;

    @Resource
    private FlcxTypeService flcxTypeService;

    @Resource
    private FlcxRecordsService flcxRecordsService;

    /** 垃圾分类查询
      * @author sgmark@aliyun.com
      * @date 2019/6/19 0019
      * @param
      * @return
      */
    @Api(name = "lex.check", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map lexCheck(FlcxBean flcxBean)throws ApiException {
        return flcxLexiconService.lexCheck(flcxBean);
    }
    /** 大分类列表
      * @author sgmark@aliyun.com
      * @date 2019/6/20 0020
      * @param
      * @return
      */
    @Api(name = "type.list", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map typeList()throws ApiException {
        return flcxTypeService.typeList();
    }
    @Api(name = "type.top5", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map topFive()throws ApiException {
        return flcxRecordsService.topFive();
    }


    @Api(name = "keySearch", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map keySearch(FlcxBean flcxBean)throws ApiException {
        return flcxLexiconService.keySearch(flcxBean);
    }

}

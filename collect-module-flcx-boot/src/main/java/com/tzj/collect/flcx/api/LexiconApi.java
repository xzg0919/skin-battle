package com.tzj.collect.flcx.api;


import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.collect.service.FlcxTypeService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Api(name = "lex.check.before", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map keySearchInRedis(){
        return flcxLexiconService.keySearchInRedis(null);
    }

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
        //发送MQ消息
        Map map = flcxLexiconService.lexCheck(flcxBean);
        if (null != map && null != map.get("flcxRecords")){
            FlcxRecords flcxRecords = (FlcxRecords) map.get("flcxRecords");
            flcxBean.setLexicon(flcxBean.getName());
            flcxBean.setCity(flcxBean.getCity());
            flcxBean.setLexiconAfter(flcxRecords.getLexiconAfter());
            flcxBean.setLexiconId(flcxRecords.getLexiconsId());
            rabbitTemplate.convertAndSend("search_keywords_queue",flcxBean);
        }
        return map;
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
        List<FlcxLexicon> flcxLexiconList = flcxLexiconService.keySearch("allCatch");
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(flcxBean.getName())){
            return map;
        }
        map.put("result", flcxLexiconList.stream().filter(flcxLexicon -> flcxLexicon.getName().contains(flcxBean.getName())).limit(5).collect(Collectors.toList()));
        return map;
    }

    /*@Api(name = "test", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public void test()throws ApiException {
        FlcxBean flcxBean = new FlcxBean();
        flcxBean.setAliUserId("12345678");
        flcxBean.setLexicon("测试");
        flcxBean.setCity("上海");
        flcxBean.setLexiconAfter("测试");
        flcxBean.setLexiconId(1L);
        flcxBean.setName("ces");
        rabbitTemplate.convertAndSend("search_keywords_queue",flcxBean);
    }*/
}

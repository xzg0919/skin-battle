package com.tzj.collect.api.lexicon.param;

import lombok.Data;

/**
 * @author sgmark
 * @create 2019-06-19 10:31
 **/
@Data
public class FlcxBean {

    private String name;//查询名称

    private Long typeId = 0L;//类型id

    private String lexicon;//查询结果关键字

    private Long lexiconId;//关键字id lexiconId

    private String lexiconAfter;//查询结果

    private String aliUserId;//用户id

    private String city;    //城市

    private String longitude;   //经度

    private String latitude;    //纬度

    private String imageUrl;//图片链接地址

    private String imageUrlAR;//Ar图片地址

    private String speechText;//语音文字
}

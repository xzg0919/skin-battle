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

    private String aliUserId;//用户id

    private String city;    //城市

    private String longitude;   //经度

    private String latitude;    //纬度
}

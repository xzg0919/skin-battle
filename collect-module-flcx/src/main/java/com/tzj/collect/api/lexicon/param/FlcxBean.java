package com.tzj.collect.api.lexicon.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sgmark
 * @create 2019-06-19 10:31
 **/
@Data
public class FlcxBean {

    private Long cityId = 0L;//城市id(查询参数)(第一版默认上海)

    private String cityName = "";//城市名称(查询参数)

    private Long typeId = 0L;//类型id(查询参数)

    private String name = "";//查询名称(查询参数)

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

    private MultipartFile headImg;//图片文件

    private String traceId;//反馈id

    private String feedbackRubbish;// 反馈名称

    private String actionType;//反馈结果

    private  PageBean pageBean;

    private boolean notCount = false;

}

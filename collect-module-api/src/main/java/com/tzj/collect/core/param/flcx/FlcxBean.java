package com.tzj.collect.core.param.flcx;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author sgmark
 * @create 2019-06-19 10:31
 **/
@Data
public class FlcxBean implements Serializable {

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

    private SourceType sourceType = SourceType.isv;//默认小程序

    private  String source;//区别来源isv/ar

    /** 调用来源
      * @author sgmark@aliyun.com
      * @date 2019/7/29 0029
      * @param
      * @return
      */
    public enum SourceType implements IEnum {
        isv(0), // 小程序
        ar_isv(1), // ar
        assistant_isv(2);//助手
        private int value;

        SourceType(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }
}

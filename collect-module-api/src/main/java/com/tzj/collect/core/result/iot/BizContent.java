package com.tzj.collect.core.result.iot;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgmark
 * @create 2020-01-17 16:06
 **/
@Data
public class BizContent implements Serializable {
    private String biz_code = "isv";//业务编码

    private String cognition_type = "Text";// 垃圾识别类型 (ImageUrl | SpeechText | Text)

    private String cognition_content;// 图片url，或者文本

    private String longitude;// 经度(否)

    private String latitude;//纬度(否)

    private String city_code;//市(否)

    private String trace_id;//反馈id

    private String feedback_rubbish;//反馈名称

    private String action_type;

    private String source;//区分ar/isv
}

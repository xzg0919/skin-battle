package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 分享码覆盖范围
 * @author: sgmark@aliyun.com
 * @Date: 2019/12/19 0019
 * @Param: 
 * @return: 
 */
@TableName("sb_line_qr_code_range")
@Data
public class LineQrCodeRange extends DataEntity<Long> {
    /**
     *  id
     */
    private Long id;

    private String shareCode;//唯一code

    private Long streetId;//街道

    private Long areaId;//区

    private Long  cityId;//市

    private Long  provinceId;//省

    private String streetName;

    private String areaName;

    private String cityName;

    private String provinceName;
}

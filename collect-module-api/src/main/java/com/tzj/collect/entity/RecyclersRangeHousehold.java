package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * 回收人员生活垃圾服务范围表
 *
 * @Author 王灿
 **/
@TableName("sb_recyclers_range_household")
@Data
public class RecyclersRangeHousehold extends DataEntity<Long> {
    private Long id;
    /**
     * 回收人员Id
     */
    private Integer recyclersId;
    /**
     * 回收企业Id
     */
    private Integer companyId;
    /**
     * 回收小区Id
     */
    private Integer communityId;
    /**
     * 回收街道Id
     */
    private Integer streetId;



}

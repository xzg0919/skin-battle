package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


/**
 *
 * 回收人员电器服务范围表
 *
 * @Author 王灿
 **/
@TableName("sb_recyclers_range_appliance")
@Data
public class RecyclersRangeAppliance extends DataEntity<Long> {
    private Long id;
    /**
     * 回收经理Id
     */
    private Integer recyclersId;
    /**
     * 所属的企业Id
     */
    private Integer companyId;
    /**
     * 所属的区域id目前到街道
     */
    private Integer streetId;
    /**
     * 所属区域的父亲节点
     */
    private String areaId;


}

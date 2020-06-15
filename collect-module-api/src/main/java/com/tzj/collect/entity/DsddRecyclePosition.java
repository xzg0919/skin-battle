package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 回收网点信息
 * @author 
 */
@Data
@TableName("dsdd_recycle_position")
public class DsddRecyclePosition extends DataEntity<Long> {
    /**
     * id
     */
    private Long id;

    /**
     * 回收点编号
     */
    private String positionNo;

    /**
     * 服务商id
     */
    private Long companyId;

    /**
     * 点位名称
     */
    private String positionName;

    /**
     * 联系电话
     */
    private String contactTel;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 地址
     */
    private String address;

    /**
     * 启用状态标志位；
     */
    private String isEnable;


}
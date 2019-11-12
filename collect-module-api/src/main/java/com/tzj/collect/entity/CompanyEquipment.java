package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 公司所对应设备
 *
 * @author sgmark
 * @create 2019-04-02 14:30
 **/
@TableName("sb_company_equipment")
@Data
public class CompanyEquipment extends  DataEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;//企业Id

    private String equipmentCode;//设备编号

    private Double longitude; // 经度

    private Double latitude; // 纬度
    /**
     * 区域id
     */
    private Integer areaId;
    /**
     * 街道id
     */
    private Integer streetId;
    /**
     * 小区id
     */
    private Integer communityId;

    private String address;
    @TableField(value = "status_")
    private Integer status;//当前状态(满仓与否)

    private String  workHours;//设备工作时间

}

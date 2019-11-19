package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 公司所对应设备错误编号
 *
 * @author sgmark
 * @create 2019-04-02 14:30
 **/
@TableName("sb_equipment_error_code")
@Data
public class EquipmentErrorCode extends  DataEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;//企业Id

    private String errorCode;//错误编号

    private String errorMessage;//错误信息
}

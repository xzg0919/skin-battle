package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 公司所对应设备错误记录
 *
 * @author sgmark
 * @create 2019-04-02 14:30
 **/
@TableName("sb_equipment_error_list")
@Data
public class EquipmentErrorList extends  DataEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;//企业Id

    private Long companyName;//企业名称

    private String equipmentCode;//设备编号（逗号分割）

    private String errorCode;//错误编号（逗号分割）

    private String errorMessage;//错误信息

    private String linkName;//联系人名称

    private String mobile;//联系电话

}

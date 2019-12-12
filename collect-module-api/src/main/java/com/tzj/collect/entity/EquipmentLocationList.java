package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 设备历史位置信息表（先进先出存10条记录）
 *
 * @author sgmark
 * @create 2019-11-12 15:27
 **/
@TableName("sb_equipment_location_list")
@Data
public class EquipmentLocationList extends  DataEntity<Long>{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String equipmentCode;//设备编号

    private String longitude; // 经度

    private String latitude; // 纬度

    private String location;//定位地址

}

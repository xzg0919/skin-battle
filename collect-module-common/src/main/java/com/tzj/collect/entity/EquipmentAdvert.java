package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * iot设备广告信息
 *
 * @author sgmark
 * @create 2019-11-12 15:35
 **/
@TableName("sb_equipment_advert")
@Data
public class EquipmentAdvert extends  DataEntity<Long>{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String equipmentCode;//设备编号

    private String picUrl;//展示图片链接

    private Date startTime;//启用时间

    private Date endTime;//停用时间
}

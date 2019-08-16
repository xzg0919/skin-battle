package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 用户领奖记录
 * @author sgmark
 * @create 2019-08-09 11:36
 **/
@TableName("daily_receiving")
@Data
public class DailyReceiving extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private String aliUserId;

    private String uuId;//红包随机id

    private Double price;//红包金额

    private Integer isReceive;//是否已领取, 默认为1: 未领取; 0:已领取

    private Integer setNum;//红包位置

    @TableField(value = "week_")
    private String week;//周数(年+周)
}

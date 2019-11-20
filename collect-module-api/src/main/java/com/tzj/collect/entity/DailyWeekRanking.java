package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 每日答答答词库
 *
 * @author sgmark
 * @create 2019-08-09 11:36
 **/
@TableName("daily_week_ranking")
@Data
public class DailyWeekRanking extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    private String linkName;

    private String city;//城市名称

    private String img;//头像

    @TableField(value = "week_")
    private String week;//年+周(2019年第35周)
}

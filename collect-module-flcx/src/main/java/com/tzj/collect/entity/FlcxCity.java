package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 城市info
 * @author sgmark
 * @create 2019-06-17 16:13
 **/
@TableName("flcx_city")
@Data
public class FlcxCity extends DataEntity<Long>{

    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;
    /**
     * 城市名称
     */
    private String  cityName;

    @TableField(value = "sort_")
    private Long sort;//所属父类

    @TableField(value = "code_")
    private String code;//区域编码

    private String type; //区域类型

    private String provider; //提供单位

    private String syndication;//联合发布

}

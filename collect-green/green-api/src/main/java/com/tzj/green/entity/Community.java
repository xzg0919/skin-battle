package com.tzj.green.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


@Data
@TableName("sb_community")
public class Community extends DataEntity<Long> {

    private Long id;

    private Integer areaId;

    private String areaIds;

    private String communityName;
    @TableField(value = "sort_")
    private Integer sort;

}

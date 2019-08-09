package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("sb_imprison_rule")
public class ImprisonRule extends DataEntity<Long> {

    private Long id;

    private Integer orderNum;

    private Integer dateNum;

    private String isEnable;

    private Integer imprisonDateNum;

    private String title;


}

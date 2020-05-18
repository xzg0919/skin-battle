package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_xy_check_street")
@Data
public class XyCheckStreet extends DataEntity<Long> {

    private Long id;

    private String streetName;

    private String streetCode;

}

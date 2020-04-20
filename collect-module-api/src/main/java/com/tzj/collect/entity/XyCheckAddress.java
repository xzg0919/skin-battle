package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_xy_check_address")
@Data
public class XyCheckAddress extends DataEntity<Long> {

    private Long id;

    private String message;






}

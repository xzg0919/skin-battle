package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.entity.DataEntity;
import lombok.Data;

@TableName("sb_jfapp_point_list")
@Data
public class JfappPointList extends DataEntity<Long> {

    private Long id;
    /**
     * 用户阿里Id
     */
    private String aliUserId;
    /**
     * 消耗的积分
     */
    private String point;
    /**
     * 类型0增加  1减少
     */
    private String type;
    /**
     * app操作员Id
     */
    private String recyclerId;
    /**
     * 用户阿里Id
     */
    private String descrb;
    //用户姓名
    private String userName;
    //用户手机号
    private String mobile;
}

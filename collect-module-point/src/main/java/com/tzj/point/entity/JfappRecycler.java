package com.tzj.point.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.entity.DataEntity;
import lombok.Data;

@TableName("sb_jfapp_recycler")
@Data
public class JfappRecycler extends DataEntity<Long> {

    private Long id;

    //姓名
    @TableField(value = "name_")
    private String name;
    //电话
    private String tel;
    //密码
    private  String password;
    //设备终端号
    private String terminalNo;


}

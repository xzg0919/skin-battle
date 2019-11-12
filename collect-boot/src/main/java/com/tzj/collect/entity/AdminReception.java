package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_admin_reception")
@Data
public class AdminReception extends DataEntity<Long> {
    private Long id;

    private String username;   //账号

    private String password;   //密码

    @TableField(value="name_")
    private String name;      //名称







}

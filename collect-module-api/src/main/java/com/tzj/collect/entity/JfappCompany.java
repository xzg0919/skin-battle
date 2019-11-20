package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tzj.collect.entity.DataEntity;
import lombok.Data;

@TableName("sb_jfapp_company")
@Data
public class JfappCompany extends DataEntity<Long> {

    private Long id;
    //企业名称
    @TableField(value = "name_")
    private String name;
    //用户名
    private String userName;
    //密码
    @TableField(value = "password_")
    private  String password;
    //企业编号
    private  String companyCode;
    //联系人
    private String contacts;
    //联系电话
    private String tel;
    //地址
    private String address;
    //简介
    private String introduction;
    //图标
    private String icon;
    //网址
    private  String website;
    //电子邮件
    private String email;
    //邮编
    private String zipcode;
    //组织机构编码
    private String orgCode;


}

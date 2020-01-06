package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收企业表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_company")
public class Company extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 企业名称
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 企业编号
     */
    private String companyCode;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 登录用户名
     */
    private String userName;
    /**
     * 登录密码
     */
    @TableField(value = "password_")
    private String password;
    /**
     * 地址
     */
    private String address;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 图标
     */
    private String icon;
    /**
     * 网址
     */
    private String website;
    /**
     * 电子邮件
     */
    private String email;

}

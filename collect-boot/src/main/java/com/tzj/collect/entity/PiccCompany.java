package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 保险企业
 */
@TableName("sb_picc_company")
public class PiccCompany extends  DataEntity<Long> {

    private Long id ;

    /**
     * 企业名
     */
    @TableField(value = "name_")
    private String  name;

    /**
     * 登录名
     */
    private String  userName;

    /**
     * 密码
     */
    private String  password;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

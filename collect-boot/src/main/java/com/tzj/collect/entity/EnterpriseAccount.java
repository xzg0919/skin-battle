package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 王灿
 * 以旧换新企业账号表
 */
@TableName("sb_enterprise_account")
public class EnterpriseAccount extends DataEntity<Long>{

    private Long id ;
    /**
     * 企业id
     */
    private Integer enterpriseId ;
    /**
     * 用户名
     */
    private String userName ;
    /**
     * 密码
     */
    @TableField(value="password_")
    private String password;
    /**
     * 状态
     */
    @TableField(value="status_")
    private String status = "0";

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

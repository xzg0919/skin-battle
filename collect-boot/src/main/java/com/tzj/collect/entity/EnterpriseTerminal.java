package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 王灿
 * 以旧换新企业终端表
 */
@TableName("sb_enterprise_terminal")
public class EnterpriseTerminal extends  DataEntity<Long>{

    private Long id ;
    /**
     * 企业id
     */
    private Integer enterpriseId;
    /**
     * 企业终端名称
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 联系人负责人
     */
    private String contacts;
    /**
     * 企业终端地址
     */
    private String address;
    /**
     * 企业终端联系电话
     */
    private String tel;
    /**
     * 企业终端密码
     */
    @TableField(value = "password_")
    private String password;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    private String startTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartTime() {
        return getDate(createDate);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Transactional
    public String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}

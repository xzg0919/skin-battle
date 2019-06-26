package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 王灿
 * 依旧换新企业表
 */
@TableName("sb_enterprise")
public class Enterprise extends  DataEntity<Long>{

    private Long id ;

    @TableField(value="name_")
    private String name;
    /**
     * 企业名称
     */
    private String enterpriseCode;
    /**
     * 企业编号
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String tel;
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
    /**
     * 邮编
     */
    private String zipcode;
    /**
     * 组织机构编码
     */
    private String orgCode;


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

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}

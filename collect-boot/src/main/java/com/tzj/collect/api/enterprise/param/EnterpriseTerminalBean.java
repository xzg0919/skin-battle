package com.tzj.collect.api.enterprise.param;

import com.tzj.collect.api.ali.param.PageBean;

public class EnterpriseTerminalBean {

    private String id ;
    /**
     * 企业id
     */
    private String enterpriseId;
    /**
     * 企业终端名称
     */
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
    private String password;

    private PageBean pageBean;
    /**
     * 短信验证码
     */
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
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
}

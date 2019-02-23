package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 第三方物流公司
 */
@TableName("sb_logistics_company")
public class LogisticsCompany extends  DataEntity<Long>{

    private Long id ;
    /**
     * 物流公司名称
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 请求该公司并推送订单的url
     */
    private String apiUrl;
    /**
     * 电话
     */
    private String tel;
    /**
     * 地址
     */
    private String address;

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

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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
}

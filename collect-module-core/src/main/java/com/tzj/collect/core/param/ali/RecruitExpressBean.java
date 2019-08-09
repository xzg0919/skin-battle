package com.tzj.collect.core.param.ali;

public class RecruitExpressBean {

    private Long id;
    /**
     * 加入方式 0服务商 1合伙人
     */
    private String type;
    /**
     * 服务商合作方式 0企业 1个人
     */
    private String cooperationType;
    /**
     * 企业名称
     */
    private String enterprise;
    /**
     * 联系人姓名
     */
    private String name;
    /**
     * 联系人手机号
     */
    private String mobile;
    /**
     * 意向开展的城市
     */
    private String city;
    /**
     * 意向回收的类型 1家电 2生活垃圾 3五公斤
     */
    private String categoryType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCooperationType() {
        return cooperationType;
    }

    public void setCooperationType(String cooperationType) {
        this.cooperationType = cooperationType;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}

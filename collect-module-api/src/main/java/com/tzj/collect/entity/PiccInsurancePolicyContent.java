package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 保险产品所属内容
 */
@TableName("sb_picc_insurance_policy_content")
public class PiccInsurancePolicyContent extends  DataEntity<Long> {

    private Long id ;

    /**
     * 所属保单Id
     */
    private  Integer insuranceId;

    /**
     * 保险额度
     */
    private  Integer insurancePrice;

    /**
     * 保险产品内容
     */
    private String content;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Integer getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(Integer insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

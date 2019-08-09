package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 保险产品所属协议
 */
@TableName("sb_picc_insurance_policy_agreement")
public class PiccInsurancePolicyAgreement extends  DataEntity<Long> {

    private Long id ;

    /**
     * 所属保单Id
     */
    private  Integer insuranceId;

    /**
     * 保险协议名称
     */
    private String agreementName;

    /**
     * 保险协议连接
     */
    private String agreementUrl;

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

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }
}

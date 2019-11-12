package com.tzj.collect.core.param.picc;

public class PiccInsurancePolicyAgreementBean {

    private String agreementId ;

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

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
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

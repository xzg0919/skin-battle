package com.tzj.collect.api.picc.param;

public class PiccInsurancePolicyContentBean {

    private String contentId ;

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

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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

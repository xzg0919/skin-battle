package com.tzj.collect.core.param.picc;

import java.util.List;

public class PiccInsurancePolicyBean {

    private String id ;

    /**
     * 保单所属公司Id
     */
    private  Integer piccCompanyId;
    /**
     * 保险产品名称
     */
    private String title;

    /**
     * 保险初始额度
     */
    private  Integer initPrice;
    /**
     * 保险承保额度
     */
    private  Integer underwritingPrice;
    /**
     * 保障协议
     */
    private List<PiccInsurancePolicyAgreementBean> piccInsurancePolicyAgreementBeanList;
    /**
     * 保障内容
     */
    private List<PiccInsurancePolicyContentBean> piccInsurancePolicyContentBeanList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPiccCompanyId() {
        return piccCompanyId;
    }

    public void setPiccCompanyId(Integer piccCompanyId) {
        this.piccCompanyId = piccCompanyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(Integer initPrice) {
        this.initPrice = initPrice;
    }

    public Integer getUnderwritingPrice() {
        return underwritingPrice;
    }

    public void setUnderwritingPrice(Integer underwritingPrice) {
        this.underwritingPrice = underwritingPrice;
    }

    public List<PiccInsurancePolicyAgreementBean> getPiccInsurancePolicyAgreementBeanList() {
        return piccInsurancePolicyAgreementBeanList;
    }

    public void setPiccInsurancePolicyAgreementBeanList(List<PiccInsurancePolicyAgreementBean> piccInsurancePolicyAgreementBeanList) {
        this.piccInsurancePolicyAgreementBeanList = piccInsurancePolicyAgreementBeanList;
    }

    public List<PiccInsurancePolicyContentBean> getPiccInsurancePolicyContentBeanList() {
        return piccInsurancePolicyContentBeanList;
    }

    public void setPiccInsurancePolicyContentBeanList(List<PiccInsurancePolicyContentBean> piccInsurancePolicyContentBeanList) {
        this.piccInsurancePolicyContentBeanList = piccInsurancePolicyContentBeanList;
    }
}

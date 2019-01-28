package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 保险产品
 */
@TableName("sb_picc_insurance_policy")
public class PiccInsurancePolicy extends  DataEntity<Long> {

    private Long id ;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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
}

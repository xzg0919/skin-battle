package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * wangcan
 * 以旧换新券码表
 */
@TableName("sb_enterprise_code")
public class EnterpriseCode extends  DataEntity<Long>{

    private static final long serialVersionUID = -5967314661200780885L;
    private Long id;
    /**
     * 企业id
     */
    private Integer enterpriseId;
    /**
     * 销售终端id
     */
    private Integer terminalId;
    /**
     * 券码
     */
    private String code;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 产品名
     */
    private String productName;
    /**
     * 抵扣金额
     */
    private BigDecimal price;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客联系电话
     */
    private String customerTel;
    /**
     * 顾客身份证号
     */
    private String customerIdcard;
    /**
     * 发票编号
     */
    private String invoiceCode;
    /**
     * 发票图片url
     */
    private String invoicePic;
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 是否使用 默认0没有 1使用 2核销
     */
    private String isUse="0";
    /**
     * 支持核销的分类Id 为空则默认全部产品可使用
     */
    private Integer categoryId;

    private Date receiveDate;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    private String receiveTime;
    /**
     * 创建时间
     */
    @TableField(exist = false)
    private String createTime;

    public String getCreateTime() {
        return getDate(createDate);
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReceiveTime() {
        return getDate(receiveDate);
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerIdcard() {
        return customerIdcard;
    }

    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoicePic() {
        return invoicePic;
    }

    public void setInvoicePic(String invoicePic) {
        this.invoicePic = invoicePic;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getDate(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}

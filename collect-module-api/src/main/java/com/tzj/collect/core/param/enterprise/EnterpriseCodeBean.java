package com.tzj.collect.core.param.enterprise;

import com.tzj.collect.core.param.ali.PageBean;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

public class EnterpriseCodeBean {

    private String id ;
    /**
     * 企业id
     */
    private String enterpriseId;
    /**
     * 销售终端id
     */
    private String terminalId;
    /**
     * 券码
     */
    private String code;
    /**
     * 商品id
     */
    private String productId;
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
    private String orderId;
    /**
     * 是否使用 默认0没有
     */
    private String isUse="0";
    /**
     * 支持核销的分类Id 为空则默认全部产品可使用
     */
    private String categoryId;
    /**
     * 短信验证码
     */
    private String captcha;

    private PageBean pageBean;

    private String startTime;

    private String endTime;

    private HttpServletResponse response;

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}

package com.tzj.collect.core.param.iot;

/**
 * @author sgmark
 * @create 2019-04-09 9:35
 **/
public class IotPostParamBean {

    private String memberId;//会员id

    private String APIName;//指令名称

    private String cabinetNo;//箱体编号

    private String qrUrl;//二维码访问地址

    private String mobile;//手机号

    private Long tranTime = System.currentTimeMillis();//当前时间

    private String sign;//签名验证

    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getTranTime() {
        return tranTime;
    }

    public void setTranTime(Long tranTime) {
        this.tranTime = tranTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }
}

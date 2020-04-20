package com.tzj.collect.core.param.app;



/**
 * 回收人员手机登录验证
 **/
public class RecyclersLoginBean {

    private String mobile;

    private String captcha;

    private String xgtoken;

    private String password;

    private String isBigRecycle;

    public String getIsBigRecycle() {
        return isBigRecycle;
    }

    public void setIsBigRecycle(String isBigRecycle) {
        this.isBigRecycle = isBigRecycle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

	public String getXgtoken() {
		return xgtoken;
	}

	public void setXgtoken(String xgtoken) {
		this.xgtoken = xgtoken;
	}
}

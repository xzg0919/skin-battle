package com.tzj.collect.api.app.param;

import com.tzj.module.easyopen.doc.annotation.ApiDocField;

/**
 * 回收人员手机登录验证
 * @Author 胡方明（12795880@qq.com）
 **/
public class RecyclersLoginBean {
    @ApiDocField(description = "手机号", required = true, example = "13801550124")
    private String mobile;
    @ApiDocField(description = "短信验证码", required = true, example = "1234")
    private String captcha;
    @ApiDocField(description = "手机硬件token", required = true, example = "111111111111")
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

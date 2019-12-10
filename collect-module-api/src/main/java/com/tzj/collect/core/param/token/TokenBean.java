package com.tzj.collect.core.param.token;

import com.tzj.module.easyopen.doc.annotation.ApiDocField;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenBean {
    @ApiDocField(description = "token")
    private String token;
    @ApiDocField(description = "token的有效期")
    private long expire;
    @ApiDocField(description = "api签名key")
    private String signKey;
    @ApiDocField(description = "蓝牙")
    private String blueTooth;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getBlueTooth() {
        return blueTooth;
    }

    public void setBlueTooth(String blueTooth) {
        this.blueTooth = blueTooth;
    }
}

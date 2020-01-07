package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 *评价表
 *
 * @Author 王灿
 **/
@TableName("sb_message")
public class Message extends  DataEntity<Long>{
    private Long id;
    /**
     * 手机号
     */
    private  String tel;
    /**
     * 短信code
     */
    private  String messageCode;
    /**
     * 有效期分钟
     */
    private  Integer validity;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

    
}

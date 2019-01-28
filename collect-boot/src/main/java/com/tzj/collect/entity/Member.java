package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 *
 *会员表
 *
 * @Author 王灿
 **/
@TableName("sb_member")
public class Member extends  DataEntity<Long>{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 身份证
     */
    private  String idCard;
    /**
     * 姓名
     */
    @TableField(value="name_")
    private  String name;
    /**
     * 地址
     */
    private  String address;
    /**
     * 绿账号
     */
    private  String greenCode;
    /**
     * 阿里userid
     */
    private  String aliUserId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 是否显示弹窗
     */
    private String isShowDialog;
    /**
     * 会员卡号
     */
    private String cardNo;
    /**
     * 阿里返回的会员卡号
     */
    private String aliCardNo;
    /**
     * 会员开卡时间
     */
    private Date openCardDate;
    /**
     * 来源的appid H5、小程序、微信等
     */
    private String appId;

    /**
     * 性别
     */
    private String gender;
    /**
     * 是否实名
     */
    private String isCertified;
    /**
     * 省份
     */
    private String city;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 昵称
     */
    private String linkName;
    /**
     * 头像
     */
    private String picUrl;

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsCertified() {
        return isCertified;
    }

    public void setIsCertified(String isCertified) {
        this.isCertified = isCertified;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIsShowDialog() {
		return isShowDialog;
	}

	public void setIsShowDialog(String isShowDialog) {
		this.isShowDialog = isShowDialog;
	}

	public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGreenCode() {
        return greenCode;
    }

    public void setGreenCode(String greenCode) {
        this.greenCode = greenCode;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAliCardNo() {
		return aliCardNo;
	}

	public void setAliCardNo(String aliCardNo) {
		this.aliCardNo = aliCardNo;
	}

	public Date getOpenCardDate() {
		return openCardDate;
	}

	public void setOpenCardDate(Date openCardDate) {
		this.openCardDate = openCardDate;
	}
    
}

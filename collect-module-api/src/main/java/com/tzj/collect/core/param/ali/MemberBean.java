package com.tzj.collect.core.param.ali;


import lombok.Data;

/**
 * 传进来的一些会员信息
 * @Author 胡方明（12795880@qq.com）
 **/
@Data
public class MemberBean {
	private String id;


    private String aliMemberId;
    private String greenSn;


    private String userName;
    private String nickName;
    
    private String certNo;
    //会员卡号
    private String cardNo;
    //用户授权的回调的code
    private String authCode;
    //用户授权的回调的code
    private String state;
    //短信验证码
    private String messageCode;
    //城市名称
    private String cityName;
	//来源  H5, HCX,
	private String source;

	private String memberId;
	private String type;
	private String message;

	private String channelId;

	private String imgUrl;

	private String aliAccount;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	//手机号
    private String mobile;
    //阿里Id
    private String aliUserId;
    
    //是否显示弹窗
    private String isShowDialog;

    public String getIsShowDialog() {
		return isShowDialog;
	}

	public void setIsShowDialog(String isShowDialog) {
		this.isShowDialog = isShowDialog;
	}

	public String getAliMemberId() {
        return aliMemberId;
    }

    public void setAliMemberId(String aliMemberId) {
        this.aliMemberId = aliMemberId;
    }

    public String getGreenSn() {
        return greenSn;
    }

    public void setGreenSn(String greenSn) {
        this.greenSn = greenSn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAliUserId() {
		return aliUserId;
	}

	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
    
}

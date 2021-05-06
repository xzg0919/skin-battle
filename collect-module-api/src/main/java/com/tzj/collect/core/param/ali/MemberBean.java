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
	//手机号
    private String mobile;
    //阿里Id
    private String aliUserId;
    //是否显示弹窗
    private String isShowDialog;

    private Double lng;//经度

	private Double lat;//纬度

	private PageBean pageBean;

    private String timestamp;//时间戳

    private String avatarUrl;

    private String city;//市

    private String gender;
    private String province;//省

    //用户授权的回调的code
    private String code;
}

package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 *
 *会员表
 *
 * @Author 王灿
 **/
@TableName("sb_member")
@Data
public class Member extends  DataEntity<Long>{
    private static final long serialVersionUID = -6405467088491154588L;
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

    private String formId;
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

    @TableField(exist = false)
    private String tableName;
    //渠道Id
    private String channelId;

}

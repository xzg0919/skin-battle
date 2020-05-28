package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 *
 *定时定点用户表
 *
 * @Author 王灿
 **/
@TableName("dsdd_member")
@Data
public class DsddMember extends DataEntity<Long> {
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
     * 省份id
     */
    private Integer provinceId;
    /**
     * 市id
     */
    private Integer cityId;
    /**
     * 区域Id
     */
    private Integer areaId;
    /**
     * 街道Id
     */
    private Integer streetId;
    /**
     * 小区Id
     */
    private Integer communityId;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 行政区名称
     */
    private String areaName;
    /**
     * 街道名称
     */
    private String streetName;
    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 详细地址
     */
    @TableField(value="address_")
    private String  address;
    /**
     * 所属区域（省+市+区+街道）
     */
    private String belongArea;

    /**
     * 阿里userid
     */
    private  String aliUserId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 会员开卡时间
     */
    private Date openCardDate;
    /**
     * 性别
     */
    private String gender;
    /**
     * 昵称
     */
    private String linkName;
    /**
     * 头像
     */
    private String picUrl;

    //定时定点实体卡
    private String dsddCardNo;
}

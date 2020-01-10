package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户会员表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_member")
public class Member extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 身份证反面
     */
    private String idCardRev;
    /**
     * 身份证正面
     */
    private String idCardObv;

    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 姓名
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 地址
     */
    private String address;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 实体卡号
     */
    private String realNo;
    /**
     * 虚拟卡号
     */
    private String emptyNo;
    /**
     * 阿里userid
     */
    private String aliUserId;
    /**
     * 是否注销
     */
    private String isCancel;
    /**
     * 省id
     */
    private Long provinceId;
    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 城市Id
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 行政区Id
     */
    private Long areaId;
    /**
     * 行政区名称
     */
    private String areaName;
    /**
     * 行政区Id
     */
    private Long streetId;
    /**
     * 行政区名称
     */
    private String streetName;
    /**
     * 所属居委Id
     */
    private Long communityId;
    /**
     * 所属居委名称
     */
    private String communityName;
    /**
     * 所属小区Id
     */
    private Long communityHouseId;
    /**
     * 所属小区名称
     */
    private String communityHouseName;

    private Long provinceId;

    private String provinceName;




}

package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_recyclers")
public class Recyclers extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 姓名
     */
    @TableField(value = "name_")
    private String name;
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
     * 头像连接
     */
    private String headPicUrl;
    /**
     * 电话
     */
    private String tel;
    /**
     * 密码
     */
    private String password;
    /**
     * 照片
     */
    private String icon;
    /**
     * 地址
     */
    private String address;
    /**
     * 性别
     */
    private String sex;
    /**
     * 芝麻认证码
     */
    private String bizNo;
    /**
     * 是否芝麻实名 0未实名 1实名
     */
    private String isReal;
    /**
     * 阿里userId 
     */
    private String aliUserId;


}

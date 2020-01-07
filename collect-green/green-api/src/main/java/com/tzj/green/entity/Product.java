package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [活动表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_product")
public class Product extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;

    private String productNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 活动名称
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 活动开始时间
     */
    private java.util.Date pickStartDate;
    /**
     * 活动结束时间
     */
    private java.util.Date pickEndDate;
    /**
     * 具体地址Id
     */
    private Long houseNameId;
    /**
     * 活动说明
     */
    private String detail;
    /**
     * 兑换总数量
     */
    private Long exchangeNum;
    /**
     * 兑换总积分
     */
    private Long exchangePoints;
    /**
     * 是否下架 0不下架  1下架
     */
    private String isLower;

    @TableField(exist = false)
    private String pickStartTime;
    @TableField(exist = false)
    private String pickEndTime;

    public String getPickStartTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(pickStartDate);
    }

    public void setPickStartTime(String pickStartTime) {
        this.pickStartTime = pickStartTime;
    }

    public String getPickEndTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(pickEndDate);
    }

    public void setPickEndTime(String pickEndTime) {
        this.pickEndTime = pickEndTime;
    }
}

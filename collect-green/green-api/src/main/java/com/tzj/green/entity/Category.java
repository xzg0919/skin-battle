package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [分类表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_category")
public class Category extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * 所有父级编号
     */
    private String parentIds;
    /**
     * 名称
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 排序
     */
    @TableField(value = "sort_")
    private Long sort;
    /**
     * 分类编码
     */
    @TableField(value = "code_")
    private String code;
    /**
     * 层级
     */
    @TableField(value = "level_")
    private Long level;
    /**
     * 图标
     */
    private String icon;
    /**
     * 增值积分
     */
    private Long addPoints;
    /**
     * 减值积分
     */
    private Long subtractPoints;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 是否计量
     */
    private String isMetering;


}

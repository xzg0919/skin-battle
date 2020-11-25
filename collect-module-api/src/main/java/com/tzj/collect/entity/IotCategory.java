package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: IotCategory
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 * <p>
 * iot回收的品类
 */
@Data
@TableName("sb_iot_category")
public class IotCategory extends DataEntity<Long> {
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


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
    private int sort;


    @TableField(value = "code_")
    /**
     * 分类编码
     */
    private String code;
    /**
     * 层级
     */
    @TableField(value = "level_")
    private int level;
    /**
     * 图标
     */
    private String icon;


    /**
     * 蚂蚁森林参数
     */
    private String aliItemType;

    /**
     * 类型
     */
    private TitleType title = TitleType.DEFUALT;


    public enum TitleType implements IEnum {
        /**
         * 初始值
         */
        DEFUALT(0),
        /**
         * 家电数码
         */
        DIGITAL(1),
        /**
         * 生活垃圾
         */
        HOUSEHOLD(2);


        private int value;

        TitleType(final int value) {
            this.value = value;
        }

        @Override
        public Serializable getValue() {
            return this.value;
        }
    }



}
